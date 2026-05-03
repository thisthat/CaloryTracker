package com.thisthatdc.calorytracker.data.settings

import java.io.OutputStream

sealed interface SettingsEvent {
    object SaveSettings: SettingsEvent
    data class SetCalories(val calories: Int): SettingsEvent
    data class SetProtein(val protein: Int): SettingsEvent
    data class SetFat(val fat: Int): SettingsEvent
    data class SetCarbs(val carbs: Int): SettingsEvent
    data class SaveDB(val outputStream: OutputStream): SettingsEvent
}