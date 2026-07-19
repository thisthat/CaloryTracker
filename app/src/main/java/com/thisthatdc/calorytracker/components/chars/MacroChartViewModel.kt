package com.thisthatdc.calorytracker.components.chars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.thisthatdc.calorytracker.data.AppDatabase
import com.thisthatdc.calorytracker.data.food.FoodDao
import com.thisthatdc.calorytracker.data.home.ViewType
import com.thisthatdc.calorytracker.util.Time
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Date

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
)


sealed interface MacroChartEvent {
    data class ChangeMacro(val filter: FilterType) : MacroChartEvent
}

class MacroChartViewModel(
    foodDao: FoodDao,
    rangeMin: Date,
    rangeMax: Date,
) : ViewModel() {

    private val _state = MutableStateFlow(MacroChartState(min = rangeMin, max = rangeMax))

    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MacroChartState(min = rangeMin, max = rangeMax)
    )

    fun onEvent(event: MacroChartEvent) {
        when (event) {
            is MacroChartEvent.ChangeMacro -> {
                _state.update { it.copy(filterType = event.filter) }
            }
        }
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
                    return MacroChartViewModel(db.foodDao, min, day) as T
                }
            }
    }
}