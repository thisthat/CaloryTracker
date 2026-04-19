package com.thisthatdc.calorytracker.data.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dao: SettingsDao
): ViewModel() {

    val settings = dao.get()

    private val _state = MutableStateFlow(SettingsState())

    val state = combine(_state, settings) {state, settings ->
        state.copy(
            calories = settings.calories
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsState())

    fun onEvent(event: SettingsEvent) {
        when(event) {
            SettingsEvent.SaveSettings -> {
                viewModelScope.launch {
                    val s = Settings(
                        settings.first().uid,
                        _state.value.calories
                    )
                    dao.upsert(s)
                }
            }
            is SettingsEvent.SetCalories -> {
                _state.update { it.copy(
                    calories = event.calories
                ) }
            }
        }
    }
}