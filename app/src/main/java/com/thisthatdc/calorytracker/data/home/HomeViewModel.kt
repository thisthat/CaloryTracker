package com.thisthatdc.calorytracker.data.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.thisthatdc.calorytracker.data.AppDatabase
import com.thisthatdc.calorytracker.data.food.FoodEatenDao
import com.thisthatdc.calorytracker.data.food.FoodState
import com.thisthatdc.calorytracker.data.settings.SettingsDao
import com.thisthatdc.calorytracker.data.settings.SettingsEvent
import com.thisthatdc.calorytracker.util.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.temporal.ChronoUnit
import java.util.Date


sealed interface HomeEvent {
    object ResetDate : HomeEvent
    object PrevDate : HomeEvent
    object NextDate : HomeEvent
    data class DeleteFood (val uid: Long): HomeEvent
}

data class HomeState(
    val maxCalories: Int = 1300,
    val maxFat: Int = 50,
    val maxProtein: Int = 100,
    val maxCarbs: Int = 100,
    val food: List<FoodState> = emptyList(),
    val day: Date = Date()
)

class HomeViewModel(
    settingsDao: SettingsDao,
    val foodEatenDao: FoodEatenDao,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    private val _settings = settingsDao.get()
    private var _food = foodEatenDao.getDate(
        Time.getStartingDayMillis(_state.value.day),
        Time.getNextStartingDayMillis(_state.value.day)
    )

    private var firstTime = true

    val state = combine(_state, _settings) { state, settings ->
        Log.d("HomeViewModel", "state refresh: ${_state.value.day}")
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
//        if(firstTime) {
//            firstTime = false
//            return@combine s.copy(food = food)
//        }
        s.copy(food = food)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeState())

    fun refresh() {
        val f = foodEatenDao.getDate(
            Time.getStartingDayMillis(_state.value.day),
            Time.getNextStartingDayMillis(_state.value.day)
        )
        viewModelScope.launch {
            f.collect { f ->
                _state.update { it.copy(food = f) }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.ResetDate -> {
                _state.update { it.copy(day = Date()) }
                refresh()
            }

            is HomeEvent.NextDate -> {
                val tomorrow = _state.value.day.toInstant().plus(1, ChronoUnit.DAYS)
                _state.update { it.copy(day = Date.from(tomorrow)) }
                refresh()
            }

            is HomeEvent.PrevDate -> {
                val tomorrow = _state.value.day.toInstant().minus(1, ChronoUnit.DAYS)
                _state.update { it.copy(day = Date.from(tomorrow)) }
                refresh()
            }
            is HomeEvent.DeleteFood -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        foodEatenDao.delete(event.uid)
                    }
                }
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
                return HomeViewModel(db.settingsDao, db.foodEatenDao) as T
            }
        }
    }
}