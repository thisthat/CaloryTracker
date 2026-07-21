package com.thisthatdc.calorytracker.components.chars

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.thisthatdc.calorytracker.data.AppDatabase
import com.thisthatdc.calorytracker.data.food.FoodEatenDao
import com.thisthatdc.calorytracker.data.food.FoodState
import com.thisthatdc.calorytracker.data.home.ViewType
import com.thisthatdc.calorytracker.data.settings.GarminCaloriesDao
import com.thisthatdc.calorytracker.data.settings.SettingsDao
import com.thisthatdc.calorytracker.util.Time
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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

data class MacroChartState(
    val filterType: FilterType = FilterType.Calories,
    val min: Date,
    val max: Date,
    val calories: LinkedHashMap<String, Long> = LinkedHashMap(),
    val maxCalories: Int = 1300,
    val maxFat: Int = 50,
    val maxProtein: Int = 100,
    val maxCarbs: Int = 100,
    val food: List<FoodState> = emptyList(),
)


sealed interface MacroChartEvent {
    data class ChangeMacro(val filter: FilterType) : MacroChartEvent
    data class ChangeDay(val day: Date) : MacroChartEvent
}

class MacroChartViewModel(
    val foodEatenDao: FoodEatenDao,
    val caloriesDao: GarminCaloriesDao,
    val settingsDao: SettingsDao,
    rangeMin: Date,
    rangeMax: Date,
) : ViewModel() {

    private val _state = MutableStateFlow(MacroChartState(min = rangeMin, max = rangeMax))
    private val _settings = settingsDao.get()
    private val _food = foodEatenDao.getDate(
        Time.getStartingDayMillis(rangeMin),
        Time.getNextStartingDayMillis(rangeMax)
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private var _calories = _state.mapLatest { state ->
        var caloriesMapping : LinkedHashMap<String, Long> = LinkedHashMap()
        for (i in 0..6) {
            val date = Time.plusDays(state.min, i)
            val ds = Time.toStringDate(Date.from(date))
            val cal = caloriesDao.get(ds).first()
            caloriesMapping[ds] = if(cal?.active != null) cal.active.toLong() else 0L
        }
        caloriesMapping
    }

    val state = combine(_state, _calories) { state, calories ->
        state.copy(calories = calories)
    }.combine(_settings) { state, settings ->
        if (settings != null) {
            return@combine state.copy(
                maxCalories = settings.calories,
                maxFat = settings.fat,
                maxProtein = settings.protein,
                maxCarbs = settings.carbs
            )
        }
        state
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

    fun mapAttr(f: FoodState): Long {
        return when (state.value.filterType) {
            FilterType.Calories -> f.calories.toLong()
            FilterType.Protein -> f.protein.toLong()
            FilterType.Fat -> f.fat.toLong()
            FilterType.Carb -> f.carbs.toLong()
        }
    }

    fun computeBars(): LinkedHashMap<String, Long> {
        val dataMapping : LinkedHashMap<String, Long> = LinkedHashMap()
        for (i in 0..6) {
            val date = Time.plusDays(state.value.min, i)
            val ds = Time.toStringDate(Date.from(date))
            dataMapping[ds] = 0L
        }
        for(f in state.value.food) {
            val ts = Time.toStringDate(f.createdAt)
            dataMapping[ts] = dataMapping.getOrDefault(ts, 0L) + mapAttr(f)
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