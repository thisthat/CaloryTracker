package com.thisthatdc.calorytracker.components.chars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.thisthatdc.calorytracker.data.AppDatabase
import com.thisthatdc.calorytracker.data.food.FoodEatenDao
import com.thisthatdc.calorytracker.data.food.FoodState
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

enum class FilterType {
    Calories,
    Protein,
    Fat,
    Carb,
}

data class DailyMacro(
    val calories: Long = 0L,
    val carbs: Long = 0L,
    val fat:  Long = 0L,
    val protein:  Long = 0L,
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
    val value: Long,
    val maxValue: Long,
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

    private val DEFAULT_DATAPOINT = MacroChartDatapoint(0L, 0L)

    private val _state = MutableStateFlow(MacroChartState(min = rangeMin, max = rangeMax))
    private val _food = foodEatenDao.getDate(
        Time.getStartingDayMillis(rangeMin),
        Time.getNextStartingDayMillis(rangeMax)
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private var _macroMapping = _state.mapLatest { state ->
        var macroMapping = LinkedHashMap<String, DailyMacro>()
        for (i in 0..6) {
            val date = Time.plusDays(state.min, i)
            val ds = Time.toStringDate(Date.from(date))
            val cal = caloriesDao.get(ds).first()
            val settings = settingsDao.get(ds).firstOrNull()
            val maxCal = settings?.calories ?: 0
            val activeCalories = if(cal?.active != null) cal.active.toLong() else 0L
            macroMapping[ds] = DailyMacro(
                calories = maxCal + activeCalories,
                carbs = settings?.carbs?.toLong() ?: 0,
                fat = settings?.fat?.toLong() ?: 0,
                protein = settings?.protein?.toLong() ?: 0
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

    private fun mapAttr(f: FoodState): Long {
        return when (state.value.filterType) {
            FilterType.Calories -> f.calories.toLong()
            FilterType.Protein -> f.protein.toLong()
            FilterType.Fat -> f.fat.toLong()
            FilterType.Carb -> f.carbs.toLong()
        }
    }

    private fun mapAttr(macros: DailyMacro): Long {
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
            dataMapping[ts] = MacroChartDatapoint(0L, mapAttr(macros))
        }
        for(f in state.value.food) {
            val ts = Time.toStringDate(f.createdAt)
            val v = dataMapping.getOrDefault(ts, DEFAULT_DATAPOINT).value + mapAttr(f)
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