package com.thisthatdc.calorytracker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.thisthatdc.calorytracker.data.food.Meals

import com.thisthatdc.calorytracker.pages.AddFood
import com.thisthatdc.calorytracker.pages.AddFoodItem
import com.thisthatdc.calorytracker.pages.AddFoodMeal
import com.thisthatdc.calorytracker.pages.Home
import com.thisthatdc.calorytracker.pages.Settings
import kotlinx.serialization.Serializable

@Serializable
data object HomeScreen : NavKey


@Serializable
data class AddFoodScreen(val meal: Meals, val day: Long) : NavKey


@Serializable
data class AddFoodItemScreen(val meal: Meals) : NavKey

@Serializable
data class AddFoodMealScreen(val meal: Meals, val foodId: Long, val day: Long) : NavKey


@Serializable
data object SettingsScreen : NavKey


@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
) {
    // initial screen
    val backStack = rememberNavBackStack(HomeScreen)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = { key ->
            when (key) {
                is HomeScreen -> {
                    NavEntry(key = key) {
                        Home(
                            onFoodClick = { meal, day ->
                                backStack.add(AddFoodScreen(meal, day))
                            },
                            onSettingsClick = {
                                backStack.add(SettingsScreen)
                            }
                        )
                    }
                }

                is AddFoodItemScreen -> {
                    NavEntry(key = key) {
                        AddFoodItem(
                            onBack = {
                                backStack.removeLastOrNull()
                            },
                        )
                    }
                }

                is AddFoodMealScreen -> {
                    NavEntry(key = key) {
                        AddFoodMeal(
                            meals = key.meal,
                            foodId = key.foodId,
                            day = key.day,
                            onBack = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }
                }

                is AddFoodScreen -> {
                    NavEntry(key = key) {
                        AddFood(
                            meal = key.meal,
                            onBack = {
                                backStack.removeLastOrNull()
                            },
                            onFoodItemClick = {
                                backStack.add(AddFoodItemScreen(key.meal))
                            },
                            onFoodSelected = { foodId ->
                                backStack.add(AddFoodMealScreen(key.meal, foodId, key.day))
                            }
                        )
                    }
                }

                is SettingsScreen -> {
                    NavEntry(key = key) {
                        Settings(
                            onBack = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }
                }

                else -> throw RuntimeException("Invalid routing key")
            }
        }
    )
}