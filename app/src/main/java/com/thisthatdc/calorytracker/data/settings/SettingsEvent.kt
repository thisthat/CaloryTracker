package com.thisthatdc.calorytracker.data.settings

sealed interface SettingsEvent {
    object SaveSettings: SettingsEvent
    data class SetCalories(val calories: Int): SettingsEvent
}