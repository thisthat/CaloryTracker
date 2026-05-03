package com.thisthatdc.calorytracker.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thisthatdc.calorytracker.components.MacroList
import com.thisthatdc.calorytracker.components.MacroListState
import com.thisthatdc.calorytracker.data.food.AddFoodMealEvent
import com.thisthatdc.calorytracker.data.food.AddFoodMealState
import com.thisthatdc.calorytracker.data.food.AddFoodMealViewModel
import com.thisthatdc.calorytracker.data.food.FoodExample
import com.thisthatdc.calorytracker.data.food.Meals
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodMeal(
    modifier: Modifier = Modifier,
    state: AddFoodMealState,
    onEvent: (AddFoodMealEvent) -> Unit,
    onBack: () -> Unit,
    day: Long,
) {
    if (state.food == null) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
        return
    }
    val ratio = state.quantity / 100f
    val macroState = MacroListState(
        caloriesStatus = "${ratio * state.food.calories}",
        proteinStatus = "${ratio * state.food.protein}",
        fatStatus = "${ratio * state.food.fat}",
        carbsStatus = "${ratio * state.food.carbs}"
    )

    val scrollBehavior = pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Add new Food")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 5.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = state.food.name,
                placeholder = { Text("Name of your food") },
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = {
                    Text("Name")
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.95f),
                value = state.food.unit.unit,
                readOnly = true,
                enabled = false,
                onValueChange = {},
                label = {
                    Text("Unit")
                },
                trailingIcon = { Icon(Icons.Outlined.ArrowDropDown, contentDescription = null) }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = state.quantity.toString(),
                onValueChange = {
                    if (it.isNotEmpty() && it.isDigitsOnly()) onEvent(
                        AddFoodMealEvent.SetQuantity(
                            it.toLong()
                        )
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text("Quantity ${state.food.unit.unit}")
                }
            )
            MacroList(
                state = macroState
            )
            Spacer(modifier = Modifier.height(10.dp))
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(0.95f),
                onClick = {
                    onEvent(AddFoodMealEvent.Save(day))
                    onBack()
                    onBack()
                }
            ) {
                Text("Save")
            }
        }
    }
}


@Composable
fun AddFoodMeal(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    meals: Meals,
    foodId: Long,
    day: Long,
) {
    val viewModel: AddFoodMealViewModel =
        viewModel(factory = AddFoodMealViewModel.Factory(meals, foodId))
    val state by viewModel.state.collectAsState()
    AddFoodMeal(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        day = day,
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun AddFoodMealPreview() {
    CaloryTrackerTheme {
        AddFoodMeal(
            onBack = {},
            state = AddFoodMealState(
                quantity = 90,
                food = FoodExample[0],
            ),
            day = System.currentTimeMillis(),
            onEvent = {}
        )
    }
}