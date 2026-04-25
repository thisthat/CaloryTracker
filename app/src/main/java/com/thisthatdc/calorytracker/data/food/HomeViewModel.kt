package com.thisthatdc.calorytracker.data.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.thisthatdc.calorytracker.data.AppDatabase
import com.thisthatdc.calorytracker.data.settings.SettingsDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


sealed interface HomeEvent {
    object ChangeDate : HomeEvent
    object NextDate : HomeEvent
}

data class HomeState(
    val maxCalories: Int = 1300,
    val maxFat: Int = 50,
    val maxProtein: Int = 100,
    val maxCarbs: Int = 100
)

class HomeViewModel(
    settingsDao: SettingsDao
) : ViewModel() {

    private val _settings = settingsDao.get()
    private val _state = MutableStateFlow(HomeState())

    val state = combine(_state, _settings) { state, settings ->
        if (settings != null) {
            _state.update {
                it.copy(
                    maxCalories = settings.calories,
                    maxFat = settings.fat,
                    maxProtein = settings.protein,
                    maxCarbs = settings.carbs
                )
            }
            _state.value
        } else {
            state
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeState())

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.ChangeDate -> {
                // todo
            }

            is HomeEvent.NextDate -> {
                //todo
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
                return HomeViewModel(db.settingsDao) as T
            }
        }
    }
}