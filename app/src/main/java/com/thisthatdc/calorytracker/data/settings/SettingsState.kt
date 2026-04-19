package com.thisthatdc.calorytracker.data.settings

data class SettingsState(
    val calories: Int = 1300,
    val fat: Int = 50,
    val protein: Int = 100,
    val carbs: Int = 100
)
