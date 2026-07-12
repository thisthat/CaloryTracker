package com.thisthatdc.calorytracker.components.chars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.thisthatdc.calorytracker.data.AppDatabase
import com.thisthatdc.calorytracker.data.food.FoodDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

enum class FilterType {
    Calories,
    Protein,
    Fat,
    Carb,
}

data class MacroChartState(
    val filterType: FilterType = FilterType.Calories,
)


sealed interface MacroChartEvent {
    data class ChangeMacro(val filter: FilterType) : MacroChartEvent
}

class MacroChartViewModel(
    foodDao: FoodDao,
) : ViewModel() {

    private val _state = MutableStateFlow(MacroChartState())

    val foods = foodDao.getAll().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MacroChartState()
    )

    fun onEvent(event: MacroChartEvent) {
        when (event) {
            is MacroChartEvent.ChangeMacro -> {
                _state.update { it.copy(filterType = event.filter) }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val db = AppDatabase.getDatabase(application)
                return MacroChartViewModel(db.foodDao) as T
            }
        }
    }
}