package com.thisthatdc.calorytracker.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.pinnedScrollBehavior
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thisthatdc.calorytracker.components.DateBar
import com.thisthatdc.calorytracker.components.DateNavigator
import com.thisthatdc.calorytracker.components.FoodList
import com.thisthatdc.calorytracker.components.MacroState
import com.thisthatdc.calorytracker.components.Macros
import com.thisthatdc.calorytracker.data.food.Meals
import com.thisthatdc.calorytracker.data.home.HomeEvent
import com.thisthatdc.calorytracker.data.home.HomeState
import com.thisthatdc.calorytracker.data.home.HomeViewModel
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme
import com.thisthatdc.calorytracker.ui.theme.FatColor
import com.thisthatdc.calorytracker.ui.theme.Purple40
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    modifier: Modifier = Modifier,
    onFoodClick: (Meals, Long) -> Unit,
    onSettingsClick: () -> Unit,
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
    val scrollBehavior = pinnedScrollBehavior(rememberTopAppBarState())
    var totalCalories = 0
    var totalFat = 0
    var totalProtein = 0
    var totalCarbs = 0
    state.food.forEach { food ->
        val ratio = food.quantity / 100f
        totalCalories += ceil((ratio * food.calories).toDouble()).toInt()
        totalFat += ceil((ratio * food.fat).toDouble()).toInt()
        totalProtein += ceil((ratio * food.protein).toDouble()).toInt()
        totalCarbs += ceil((ratio * food.carbs).toDouble()).toInt()
    }

    val macroState = MacroState(
        currentCalories = totalCalories,
        maxCalories = state.maxCalories,
        currentFat = totalFat,
        maxFat = state.maxFat,
        currentProtein = totalProtein,
        maxProtein = state.maxProtein,
        currentCarbs = totalCarbs,
        maxCarbs = state.maxCarbs
    )

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Calory Tracker", color = Purple40)
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = Purple40,
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DateBar(modifier = modifier
                .padding(top = 10.dp)
                .fillMaxWidth(0.95f))
            DateNavigator(
                modifier = modifier,
                day = state.day,
                onPrev = { onEvent(HomeEvent.PrevDate) },
                onNext = { onEvent(HomeEvent.NextDate) },
                onReset = { onEvent(HomeEvent.ResetDate) }
            )
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp)
            ) {
                item {
                    Macros(modifier, macroState)
                    Spacer(Modifier.height(10.dp))
                    FoodList(modifier, onFoodClick, state)
                }
            }
        }
    }
}

@Composable
fun Home(
    modifier: Modifier = Modifier,
    onFoodClick: (Meals, Long) -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val state by viewModel.state.collectAsState()
    Home(
        state = state,
        onEvent = viewModel::onEvent,
        onFoodClick = onFoodClick,
        onSettingsClick = onSettingsClick,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CaloryTrackerTheme {
        Home(
            onFoodClick = { _: Meals, _: Long -> },
            onSettingsClick = {},
            state = HomeState(),
            onEvent = {}
        )
    }
}