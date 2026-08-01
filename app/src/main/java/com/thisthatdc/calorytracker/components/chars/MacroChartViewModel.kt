package com.thisthatdc.calorytracker.components.chars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.thisthatdc.calorytracker.data.AppDatabase
import com.thisthatdc.calorytracker.data.food.FoodEatenDao
import com.thisthatdc.calorytracker.data.food.FoodState
import com.thisthatdc.calorytracker.data.food.Unit.GENERIC
import com.thisthatdc.calorytracker.data.home.ViewType
import com.thisthatdc.calorytracker.data.settings.GarminCaloriesDao
import com.thisthatdc.calorytracker.data.settings.Settings
import com.thisthatdc.calorytracker.data.settings.SettingsDao
import com.thisthatdc.calorytracker.util.Time
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Date
import kotlin.collections.LinkedHashMap
import kotlin.math.ceil

enum class FilterType {
    Calories,
    Protein,
    Fat,
    Carb,
}

data class DailyMacro(
    val calories: Float = 0f,
    val carbs: Float = 0f,
    val fat:  Float = 0f,
    val protein:  Float = 0f,
)

data class MacroChartState(
    val filterType: FilterType = FilterType.Calories,
    val min: Date,
    val max: Date,
    val calories: LinkedHashMap<String, Long> = LinkedHashMap(),
    val macros: Map<String, DailyMacro> = LinkedHashMap(),
    val food: List<FoodState> = emptyList(),
)

data class MacroChartDatapoint(
    val value: Double,
    val maxValue: Double,
)


sealed interface MacroChartEvent {
    data class ChangeMacro(val filter: FilterType) : MacroChartEvent
    data class ChangeDay(val day: Date) : MacroChartEvent
}

class MacroChartViewModel(
    foodEatenDao: FoodEatenDao,
    val caloriesDao: GarminCaloriesDao,
    val settingsDao: SettingsDao,
    rangeMin: Date,
    rangeMax: Date,
) : ViewModel() {

    private val DEFAULT_DATAPOINT = MacroChartDatapoint(0.0, 0.0)

    private val _state = MutableStateFlow(MacroChartState(min = rangeMin, max = rangeMax))

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _food = _state.mapLatest { state ->
        foodEatenDao.getDate(
            Time.getStartingDayMillis(state.min),
            Time.getNextStartingDayMillis(state.max)
        ).first()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private var _macroMapping = _state.mapLatest { state ->
        var macroMapping = LinkedHashMap<String, DailyMacro>()
        for (i in 0..6) {
            val date = Time.plusDays(state.min, i)
            val ds = Time.toStringDate(Date.from(date))
            val cal = caloriesDao.get(ds).first()
            val settings = settingsDao.get(ds).firstOrNull()
            val maxCal = settings?.calories?.toFloat() ?: 0f
            val activeCalories = if(cal?.active != null) cal.active else 0f
            macroMapping[ds] = DailyMacro(
                calories = maxCal + activeCalories,
                carbs = settings?.carbs?.toFloat() ?: 0f,
                fat = settings?.fat?.toFloat() ?: 0f,
                protein = settings?.protein?.toFloat() ?: 0f
            )
        }
        macroMapping
    }

    val state = combine(_state, _macroMapping) { state, macrosMapping ->
        state.copy(macros = macrosMapping)
    }.combine(_food) { s, food ->
        s.copy(food = food)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MacroChartState(min = rangeMin, max = rangeMax)
    )

    fun onEvent(event: MacroChartEvent) {
        when (event) {
            is MacroChartEvent.ChangeMacro -> {
                _state.update { it.copy(filterType = event.filter) }
            }

            is MacroChartEvent.ChangeDay -> {
                val min = Date.from(Time.minusDays(event.day, 6))
                _state.update { it.copy(min = min, max = event.day) }
            }
        }
    }

    private fun mapAttr(f: FoodState): Float {
        val ratio = if(f.unit == GENERIC) f.quantity / 1f else f.quantity / 100f
        val value = when (state.value.filterType) {
            FilterType.Calories -> f.calories.toFloat()
            FilterType.Protein -> f.protein
            FilterType.Fat -> f.fat
            FilterType.Carb -> f.carbs
        }
        return (ratio * value)
    }

    private fun mapAttr(macros: DailyMacro): Float {
        return when (state.value.filterType) {
            FilterType.Calories -> macros.calories
            FilterType.Protein -> macros.protein
            FilterType.Fat -> macros.fat
            FilterType.Carb -> macros.carbs
        }
    }

    fun computeBars(): LinkedHashMap<String, MacroChartDatapoint> {
        val dataMapping : LinkedHashMap<String, MacroChartDatapoint> = LinkedHashMap()
        for (i in 0..6) {
            val date = Time.plusDays(state.value.min, i)
            val ts = Time.toStringDate(Date.from(date))
            val macros = state.value.macros.getOrDefault(ts, DailyMacro())
            dataMapping[ts] = MacroChartDatapoint(0.0, mapAttr(macros).toDouble())
        }
        for(f in state.value.food) {
            val ts = Time.toStringDate(f.createdAt)
            val v = ceil(dataMapping.getOrDefault(ts, DEFAULT_DATAPOINT).value + mapAttr(f))
            val max = dataMapping.getOrDefault(ts, DEFAULT_DATAPOINT).maxValue
            dataMapping[ts] = MacroChartDatapoint(v, max)
        }
        return dataMapping
    }

    companion object {
        fun Factory(day: Date, viewType: ViewType): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    val application =
                        checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                    val db = AppDatabase.getDatabase(application)
                    val min = Date.from(Time.minusDays(day, 6))
                    return MacroChartViewModel(
                        db.foodEatenDao,
                        db.garminCalories,
                        db.settingsDao,
                        min,
                        day
                    ) as T
                }
            }
    }
}