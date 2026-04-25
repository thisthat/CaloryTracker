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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.pinnedScrollBehavior
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.thisthatdc.calorytracker.components.Meals
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    modifier: Modifier = Modifier,
    onFoodClick: (Meals) -> Unit,
    onSettingsClick: () -> Unit,
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
    val scrollBehavior = pinnedScrollBehavior(rememberTopAppBarState())
    // TODO: fetch the current calories summing the food
    val macroState = MacroState(
        maxCalories = state.maxCalories,
        maxFat = state.maxFat,
        maxProtein = state.maxProtein,
        maxCarbs = state.maxCarbs
    )
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Calory Tracker")
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings"
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
            DateBar(modifier = modifier.padding(top = 10.dp))
            DateNavigator()
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp)
            ) {
                item {
                    Macros(modifier, macroState)
                    Spacer(Modifier.height(10.dp))
                    FoodList(modifier, onFoodClick)
                }
            }
        }
    }
}

@Composable
fun Home(
    modifier: Modifier = Modifier,
    onFoodClick: (Meals) -> Unit,
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
            onFoodClick = {},
            onSettingsClick = {},
            state = HomeState(),
            onEvent = {}
        )
    }
}