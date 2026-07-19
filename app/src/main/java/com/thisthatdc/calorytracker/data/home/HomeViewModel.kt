package com.thisthatdc.calorytracker.data.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.thisthatdc.calorytracker.data.AppDatabase
import com.thisthatdc.calorytracker.data.food.FoodEatenDao
import com.thisthatdc.calorytracker.data.food.FoodState
import com.thisthatdc.calorytracker.data.settings.GarminCalories
import com.thisthatdc.calorytracker.data.settings.GarminCaloriesDao
import com.thisthatdc.calorytracker.data.settings.SettingsDao
import com.thisthatdc.calorytracker.util.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.temporal.ChronoUnit
import java.util.Date


sealed interface HomeEvent {
    object ResetDate : HomeEvent
    object PrevDate : HomeEvent
    object NextDate : HomeEvent
    data class DeleteFood(val uid: Long) : HomeEvent

    data class GarminData(val json: String) : HomeEvent
    object GarminError : HomeEvent

    data class ChangeViewType(val viewType: ViewType) : HomeEvent
}

enum class ViewType {
    Day,
    Week,
    Month,
    Year,
}

data class HomeState(
    val maxCalories: Int = 1300,
    val maxFat: Int = 50,
    val maxProtein: Int = 100,
    val maxCarbs: Int = 100,
    val food: List<FoodState> = emptyList(),
    val day: Date = Date(),
    val isGarminLoading: Boolean = true,
    val isGarminError: Boolean = false,
    val activeKilocalories: Float = 0.0f,
    val bmrKilocalories: Float = 0.0f,
    val viewType: ViewType = ViewType.Day,
)

class HomeViewModel(
    settingsDao: SettingsDao,
    val foodEatenDao: FoodEatenDao,
    val caloriesDao: GarminCaloriesDao,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    private val _settings = settingsDao.get()

    @OptIn(ExperimentalCoroutinesApi::class)
    private var _food = _state.mapLatest { state ->
        foodEatenDao.getDate(
            Time.getStartingDayMillis(state.day),
            Time.getNextStartingDayMillis(state.day)
        ).first()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private var _calories = _state.mapLatest { state ->
        caloriesDao.get(Time.toStringDate(state.day)).first()
    }

    val state = combine(_state, _settings) { state, settings ->
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
    }.combine(_calories) { s, calories ->
        if (calories != null) {
            s.copy(activeKilocalories = calories.active, bmrKilocalories = calories.rest)
        } else {
            s.copy(activeKilocalories = 0f, bmrKilocalories = 0f)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeState())

    fun refresh() {
        val f = foodEatenDao.getDate(
            Time.getStartingDayMillis(_state.value.day),
            Time.getNextStartingDayMillis(_state.value.day)
        )
        val c = caloriesDao.get(Time.toStringDate(_state.value.day))
        viewModelScope.launch {
            f.collect { f ->
                _state.update { it.copy(food = f) }
            }
            c.collect { c ->
                _state.update { it.copy(activeKilocalories = c?.active ?: 0f, bmrKilocalories = c?.rest ?: 0f) }
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
                val view = _state.value.viewType
                if(view == ViewType.Day || view == ViewType.Week) {
                    val offset = if (view == ViewType.Day) 1 else 7
                    val next = Time.plusDays(_state.value.day, offset)
                    _state.update { it.copy(day = Date.from(next)) }
                    refresh()
                }
            }

            is HomeEvent.PrevDate -> {
                val view = _state.value.viewType
                if(view == ViewType.Day || view == ViewType.Week) {
                    val offset = if (view == ViewType.Day) 1 else 7
                    val prev = Time.minusDays(_state.value.day, offset)
                    _state.update { it.copy(day = Date.from(prev)) }
                    refresh()
                }
            }

            is HomeEvent.DeleteFood -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        foodEatenDao.delete(event.uid)
                    }
                }
                refresh()
            }

            is HomeEvent.GarminError -> {
                _state.update { it.copy(isGarminError = true, isGarminLoading = false) }
            }

            is HomeEvent.GarminData -> {
                //data in event.json
                val json = JSONObject(event.json)
                val activeObj = json.get("activeKilocalories").toString()
                val restingObj = json.get("bmrKilocalories").toString()
                val active = if(activeObj == "null") 0f else activeObj.toFloat()
                val resting = if(restingObj == "null") 0f else restingObj.toFloat()
                val newData = GarminCalories(Time.toStringDate(_state.value.day), active, resting)
                _state.update {
                    it.copy(
                        isGarminLoading = false,
                        activeKilocalories = active,
                        bmrKilocalories = resting
                    )
                }
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        caloriesDao.upsert(newData)
                    }
                }
                refresh()
            }

            is HomeEvent.ChangeViewType -> {
                _state.update { it.copy(viewType = event.viewType) }
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
                return HomeViewModel(db.settingsDao, db.foodEatenDao, db.garminCalories) as T
            }
        }
    }
}