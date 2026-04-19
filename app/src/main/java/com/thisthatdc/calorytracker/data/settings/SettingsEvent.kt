package com.thisthatdc.calorytracker.data.settings

sealed interface SettingsEvent {
    object SaveSettings: SettingsEvent
    data class SetCalories(val calories: Int): SettingsEvent
    data class SetProtein(val protein: Int): SettingsEvent
    data class SetFat(val fat: Int): SettingsEvent
    data class SetCarbs(val carbs: Int): SettingsEvent
}