package com.thisthatdc.calorytracker.data.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.thisthatdc.calorytracker.data.AppDatabase
import com.thisthatdc.calorytracker.data.food.FoodDao
import com.thisthatdc.calorytracker.data.food.FoodEatenDao
import com.thisthatdc.calorytracker.util.JsonWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val dao: SettingsDao,
    private val foodDao: FoodDao,
    private val foodEatenDao: FoodEatenDao,
) : ViewModel() {

    private val _settings = dao.get()
    private var firstLoad = true;
    private val _state = MutableStateFlow(SettingsState())

    val state = combine(_state, _settings) { state, settings ->
        Log.d("SettingsViewModel", "state=$state, settings=$settings")
        if (firstLoad && settings != null) {
            firstLoad = false;
            _state.update {
                it.copy(
                    calories = settings.calories,
                    fat = settings.fat,
                    protein = settings.protein,
                    carbs = settings.carbs,
                    username = settings.username,
                    password = settings.password,
                )
            }
            _state.value
        } else {
            _state.value
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsState())

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.SaveSettings -> {
                viewModelScope.launch {
                    val currentSettings = _settings.firstOrNull()
                    val s = Settings(
                        uid = currentSettings?.uid ?: 0,
                        calories = _state.value.calories,
                        fat = _state.value.fat,
                        protein = _state.value.protein,
                        carbs = _state.value.carbs,
                        username = _state.value.username,
                        password = _state.value.password,
                    )
                    withContext(Dispatchers.IO) {
                        dao.upsert(s)
                    }
                }
            }

            is SettingsEvent.SetCalories -> {
                _state.update {
                    it.copy(
                        calories = event.calories
                    )
                }
            }

            is SettingsEvent.SetCarbs -> {
                _state.update {
                    it.copy(
                        carbs = event.carbs
                    )
                }
            }

            is SettingsEvent.SetFat -> {
                _state.update {
                    it.copy(
                        fat = event.fat
                    )
                }
            }

            is SettingsEvent.SetProtein -> {
                _state.update {
                    it.copy(
                        protein = event.protein
                    )
                }
            }

            is SettingsEvent.SetPassword -> {
                _state.update {
                    it.copy(
                        password = event.password
                    )
                }
            }

            is SettingsEvent.SetUsername -> {
                _state.update {
                    it.copy(
                        username = event.username
                    )
                }
            }

            is SettingsEvent.SaveDB -> {
                try {
                    JsonWriter.write(event.outputStream, dao.get(), foodDao.getAll(), foodEatenDao.getAll())
                } catch (e: Throwable) {
                    Log.w("Json DB Exporter", e)
                } finally {
                    event.outputStream.flush()
                    event.outputStream.close()
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
                return SettingsViewModel(db.settingsDao, db.foodDao, db.foodEatenDao) as T
            }
        }
    }
}