package com.thisthatdc.calorytracker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.thisthatdc.calorytracker.components.Meals
import com.thisthatdc.calorytracker.pages.AddFood
import com.thisthatdc.calorytracker.pages.Home
import kotlinx.serialization.Serializable

@Serializable
data object HomeScreen: NavKey


@Serializable
data class AddFoodScreen(val meal: Meals): NavKey

@Serializable
data class NoteDetail(val id: Int): NavKey

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
            when(key) {
                is HomeScreen -> {
                    NavEntry(key = key) {
                        Home(
                            onFoodClick = { meal ->
                                backStack.add(AddFoodScreen(meal))
                            }
                        )
                    }
                }
                is AddFoodScreen -> {
                    NavEntry(key = key) {
                        AddFood(meal = key.meal)
                    }
                }
                else -> throw RuntimeException("Invalid routing key")
            }
        }
    )
}