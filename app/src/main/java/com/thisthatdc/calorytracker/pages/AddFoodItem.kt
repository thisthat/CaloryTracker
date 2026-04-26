package com.thisthatdc.calorytracker.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thisthatdc.calorytracker.data.food.AddFoodItemEvent
import com.thisthatdc.calorytracker.data.food.AddFoodItemState
import com.thisthatdc.calorytracker.data.food.AddFoodItemViewModel
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodItem(
    modifier: Modifier = Modifier,
    state: AddFoodItemState,
    onEvent: (AddFoodItemEvent) -> Unit,
    onBack: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = state.name,
                placeholder = { Text("Name of your food") },
                onValueChange = { onEvent(AddFoodItemEvent.SetName(it)) },
                label = {
                    Text("Name")
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .clickable {
                        expanded = true
                    },
                value = state.unit.unit,
                readOnly = true,
                enabled = false,
                onValueChange = { if (it.isNotEmpty() && it.isDigitsOnly()) onEvent(AddFoodItemEvent.SetCalories(it.toInt())) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text("Unit")
                },
                trailingIcon = { Icon(Icons.Outlined.ArrowDropDown, contentDescription = null) }
            )
            Box(contentAlignment = Alignment.TopStart) {
                DropdownMenu(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(com.thisthatdc.calorytracker.data.food.Unit.GRAMS.unit) },
                        onClick = {
                            onEvent(AddFoodItemEvent.SetUnit(com.thisthatdc.calorytracker.data.food.Unit.GRAMS))
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(com.thisthatdc.calorytracker.data.food.Unit.LIQUID.unit) },
                        onClick = {
                            onEvent(AddFoodItemEvent.SetUnit(com.thisthatdc.calorytracker.data.food.Unit.LIQUID))
                            expanded = false
                        }
                    )
                }
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = state.calories.toString(),
                onValueChange = { if (it.isNotEmpty() && it.isDigitsOnly()) onEvent(AddFoodItemEvent.SetCalories(it.toInt())) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text("Calories 100${state.unit.unit}")
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = state.carbs.toString(),
                onValueChange = { if (it.isNotEmpty() && it.isDigitsOnly()) onEvent(AddFoodItemEvent.SetCarbs(it.toInt())) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text("Carbs 100${state.unit.unit}")
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = state.fat.toString(),
                onValueChange = { if (it.isNotEmpty() && it.isDigitsOnly()) onEvent(AddFoodItemEvent.SetFat(it.toInt())) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text("Fat 100${state.unit.unit}")
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = state.protein.toString(),
                onValueChange = { if (it.isNotEmpty() && it.isDigitsOnly()) onEvent(AddFoodItemEvent.SetProtein(it.toInt())) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text("Protein 100${state.unit.unit}")
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = state.sugar.toString(),
                onValueChange = { if (it.isNotEmpty() && it.isDigitsOnly()) onEvent(AddFoodItemEvent.SetSugar(it.toInt())) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text("Sugar 100${state.unit.unit}")
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = state.fiber.toString(),
                onValueChange = { if (it.isNotEmpty() && it.isDigitsOnly()) onEvent(AddFoodItemEvent.SetFiber(it.toInt())) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text("Fiber 100${state.unit.unit}")
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(0.95f),
                onClick = { onEvent(AddFoodItemEvent.Save) }
            ) {
                Text("Save")
            }
        }
    }
}


@Composable
fun AddFoodItem(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: AddFoodItemViewModel = viewModel(factory = AddFoodItemViewModel.Factory)
) {
    val state by viewModel.state.collectAsState()
    AddFoodItem(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun AddFoodItemPreview() {
    CaloryTrackerTheme {
        AddFoodItem(onBack = {}, state = AddFoodItemState(), onEvent = {})
    }
}