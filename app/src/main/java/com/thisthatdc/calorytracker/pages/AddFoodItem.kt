package com.thisthatdc.calorytracker.pages

import android.util.Log
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

    var calString by remember { mutableStateOf("") }
    var calError by remember { mutableStateOf(true) }

    val regex = "^[0-9]*$".toRegex()

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
                onValueChange = {
                    if (it.isNotEmpty() && it.isDigitsOnly()) onEvent(
                        AddFoodItemEvent.SetCalories(
                            it.toInt()
                        )
                    )
                },
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
                value = calString,
                onValueChange = { newStringValue ->
                    // empty == 0
                    if (newStringValue == "") {
                        calString = newStringValue
                        calError = true;
                        AddFoodItemEvent.SetCalories(0)
                        return@OutlinedTextField
                    }
                    // not a valid int
                    if (!regex.matches(newStringValue)) {
                        calError = true
                        AddFoodItemEvent.SetCalories(0)
                        return@OutlinedTextField
                    }
                    calError = false;
                    calString = newStringValue
                    try {
                        val v = newStringValue.toInt()
                        Log.d("Test","Valore: $v")
                        if (v > 0) {
                            AddFoodItemEvent.SetCalories(v)
                        } else {
                            calError = true
                        }
                    } catch (_: NumberFormatException) {
                        AddFoodItemEvent.SetCalories(0)
                        calError = true
                    }

                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text("Calories 100${state.unit.unit}")
                },
                isError = calError,
                supportingText = {
                    if (calError) {
                        Text("Incorrect number format. Must be > 0")
                    }
                }
            )
            MacroText(
                macroName = "Carbs",
                unit = state.unit.unit,
                onError = { v ->
                    AddFoodItemEvent.SetCarbs(0f)
                },
                onEvent = { v ->
                    onEvent(
                        AddFoodItemEvent.SetCarbs(v)
                    )
                })
            MacroText(
                macroName = "Fat",
                unit = state.unit.unit,
                onError = { v ->
                    AddFoodItemEvent.SetFat(0f)
                },
                onEvent = { v ->
                    onEvent(
                        AddFoodItemEvent.SetFat(v)
                    )
                })
            MacroText(
                macroName = "Protein",
                unit = state.unit.unit,
                onError = { v ->
                    AddFoodItemEvent.SetProtein(0f)
                },
                onEvent = { v ->
                    onEvent(
                        AddFoodItemEvent.SetProtein(v)
                    )
                })

            MacroText(
                macroName = "Sugar",
                unit = state.unit.unit,
                onError = { v ->
                    AddFoodItemEvent.SetSugar(0f)
                },
                onEvent = { v ->
                    onEvent(
                        AddFoodItemEvent.SetSugar(v)
                    )
                })

            MacroText(
                macroName = "Fiber",
                unit = state.unit.unit,
                onError = { v ->
                    AddFoodItemEvent.SetFiber(0f)
                },
                onEvent = { v ->
                    onEvent(
                        AddFoodItemEvent.SetFiber(v)
                    )
                })

            Spacer(modifier = Modifier.height(10.dp))
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(0.95f),
                onClick = {
                    onEvent(AddFoodItemEvent.Save)
                    onBack()
                }
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
fun MacroText(macroName: String, unit: String, onError: (Float) -> Unit, onEvent: (Float) -> Unit) {
    var macroString by remember { mutableStateOf("") }
    var macroError by remember { mutableStateOf(false) }
    val regex = "^[-+]?[0-9]*\\.?[0-9]+$".toRegex()
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(0.95f),
        value = macroString,
        onValueChange = { newStringValue ->
            // empty == 0
            if (newStringValue == "") {
                macroString = newStringValue
                macroError = false;
                onError(0f)
                return@OutlinedTextField
            }
            //ends with . we keep it going
            if (newStringValue.last() == '.' && newStringValue.count { it == '.' } == 1) {
                macroString = newStringValue
                macroError = false;
                return@OutlinedTextField
            }
            // not a valid float
            if (!regex.matches(newStringValue)) {
                macroError = true
                onError(0f)
                return@OutlinedTextField
            }
            macroError = false;
            macroString = newStringValue
            try {
                val v = newStringValue.toFloat()
                if (v >= 0) {
                    onEvent(v)
                }
            } catch (_: NumberFormatException) {
                onError(0f)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = {
            Text("$macroName 100${unit}")
        },
        isError = macroError,
        supportingText = {
            if (macroError) {
                Text("Incorrect number format.")
            }
        }
    )
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