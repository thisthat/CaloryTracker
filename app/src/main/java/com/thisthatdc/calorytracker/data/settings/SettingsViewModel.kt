package com.thisthatdc.calorytracker.data.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.thisthatdc.calorytracker.data.AppDatabase
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
    private val dao: SettingsDao
): ViewModel() {

    private val _settings = dao.get()
    private val _state = MutableStateFlow(SettingsState())

    val state = combine(_state, _settings) { state, settings ->
        if (state.calories == 1300 && settings != null) {
             state.copy(calories = settings.calories)
        } else {
            state
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsState())

    fun onEvent(event: SettingsEvent) {
        when(event) {
            SettingsEvent.SaveSettings -> {
                viewModelScope.launch {
                    val currentSettings = _settings.firstOrNull()
                    val s = Settings(
                        uid = currentSettings?.uid ?: 0,
                        calories = _state.value.calories
                    )
                    withContext(Dispatchers.IO) {
                        dao.upsert(s)
                    }
                }
            }
            is SettingsEvent.SetCalories -> {
                _state.update { it.copy(
                    calories = event.calories
                ) }
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
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val db = AppDatabase.getDatabase(application)
                return SettingsViewModel(db.settingsDao) as T
            }
        }
    }
}