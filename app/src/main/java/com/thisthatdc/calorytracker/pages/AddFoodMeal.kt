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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thisthatdc.calorytracker.components.MacroList
import com.thisthatdc.calorytracker.components.MacroListState
import com.thisthatdc.calorytracker.data.food.AddFoodMealEvent
import com.thisthatdc.calorytracker.data.food.AddFoodMealState
import com.thisthatdc.calorytracker.data.food.AddFoodMealViewModel
import com.thisthatdc.calorytracker.data.food.FoodExample
import com.thisthatdc.calorytracker.data.food.Meals
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme
import com.thisthatdc.calorytracker.data.food.Unit.GENERIC


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
    val ratio = if (state.food.unit == GENERIC) state.quantity / 1f else state.quantity / 100f
    val macroState = MacroListState(
        caloriesStatus = "${ratio * state.food.calories}",
        proteinStatus = "${ratio * state.food.protein}",
        fatStatus = "${ratio * state.food.fat}",
        carbsStatus = "${ratio * state.food.carbs}"
    )

    var quantityString by remember { mutableStateOf("") }
    var quantityError by remember { mutableStateOf(true) }
    val regex = "^[-+]?[0-9]*\\.?[0-9]+$".toRegex()

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
                value = quantityString,
                onValueChange = { newStringValue ->
                    // empty == 0
                    if (newStringValue == "") {
                        quantityString = newStringValue
                        quantityError = true;
                        onEvent(AddFoodMealEvent.SetQuantity(0f))
                        return@OutlinedTextField
                    }
                    if (newStringValue.last() == '.' && newStringValue.count { it == '.' } == 1) {
                        quantityString = newStringValue
                        quantityError = false;
                        return@OutlinedTextField
                    }
                    // not a valid float
                    if (!regex.matches(newStringValue)) {
                        quantityError = true
                        onEvent(AddFoodMealEvent.SetQuantity(0f))
                        return@OutlinedTextField
                    }
                    quantityError = false;
                    quantityString = newStringValue
                    try {
                        val v = newStringValue.toFloat()
                        if (v > 0) {
                            onEvent(AddFoodMealEvent.SetQuantity(v))
                        } else {
                            quantityError = true
                        }
                    } catch (_: NumberFormatException) {
                        onEvent(AddFoodMealEvent.SetQuantity(0f))
                        quantityError = true
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text("Quantity ${state.food.unit.unit}")
                },
                isError = quantityError,
                supportingText = {
                    if (quantityError) {
                        Text("Incorrect number format. Must be > 0")
                    }
                }
            )
            MacroList(
                state = macroState
            )
            Spacer(modifier = Modifier.height(10.dp))
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(0.95f),
                onClick = {
                    if(state.quantity > 0) {
                        onEvent(AddFoodMealEvent.Save(day))
                        onBack()
                        onBack()
                    }
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
                quantity = 90f,
                food = FoodExample[0],
            ),
            day = System.currentTimeMillis(),
            onEvent = {}
        )
    }
}