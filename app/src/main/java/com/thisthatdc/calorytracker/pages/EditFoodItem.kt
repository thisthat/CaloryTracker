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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
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
import com.thisthatdc.calorytracker.data.food.EditFoodItemEvent
import com.thisthatdc.calorytracker.data.food.EditFoodItemState
import com.thisthatdc.calorytracker.data.food.EditFoodItemViewModel
import com.thisthatdc.calorytracker.data.food.Unit.GENERIC
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFoodItem(
    modifier: Modifier = Modifier,
    state: EditFoodItemState,
    onEvent: (EditFoodItemEvent) -> Unit,
    onBack: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var calError by remember { mutableStateOf(false) }

    if(state.food == null) {
        return CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }


    var name by remember { mutableStateOf(state.food.name) }
    val regex = "^[0-9]*$".toRegex()

    var calString by remember { mutableStateOf(state.calories.toString()) }

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
                    Text("Edit $name")
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
                onValueChange = { onEvent(EditFoodItemEvent.SetName(it)) },
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
                        EditFoodItemEvent.SetCalories(
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
                            onEvent(EditFoodItemEvent.SetUnit(com.thisthatdc.calorytracker.data.food.Unit.GRAMS))
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(com.thisthatdc.calorytracker.data.food.Unit.LIQUID.unit) },
                        onClick = {
                            onEvent(EditFoodItemEvent.SetUnit(com.thisthatdc.calorytracker.data.food.Unit.LIQUID))
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(GENERIC.unit) },
                        onClick = {
                            onEvent(EditFoodItemEvent.SetUnit(GENERIC))
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
                        calError = true
                        onEvent(EditFoodItemEvent.SetCalories(0))
                        return@OutlinedTextField
                    }
                    // not a valid int
                    if (!regex.matches(newStringValue)) {
                        calError = true
                        onEvent(EditFoodItemEvent.SetCalories(0))
                        return@OutlinedTextField
                    }
                    calError = false
                    calString = newStringValue
                    try {
                        val v = newStringValue.toInt()
                        if (v > 0) {
                            onEvent(EditFoodItemEvent.SetCalories(v))
                        } else {
                            calError = true
                        }
                    } catch (_: NumberFormatException) {
                        onEvent(EditFoodItemEvent.SetCalories(0))
                        calError = true
                    }

                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text("Calories ${if (state.unit == GENERIC) "1" else "100"}${state.unit.unit}")
                },
                isError = calError,
                supportingText = {
                    if (calError) {
                        Text("Incorrect number format. Must be > 0")
                    }
                }
            )
            EditMacroText(
                macroName = "Fat",
                unit = state.unit.unit,
                onError = { v ->
                    onEvent(EditFoodItemEvent.SetFat(0f))
                },
                onEvent = { v ->
                    onEvent(
                        EditFoodItemEvent.SetFat(v)
                    )
                },
                initMacroString = state.fat.toString(),
            )
            EditMacroText(
                macroName = "Carbs",
                unit = state.unit.unit,
                onError = { v ->
                    onEvent(EditFoodItemEvent.SetCarbs(0f))
                },
                onEvent = { v ->
                    onEvent(
                        EditFoodItemEvent.SetCarbs(v)
                    )
                },
                initMacroString = state.carbs.toString(),
            )
            EditMacroText(
                macroName = "Protein",
                unit = state.unit.unit,
                onError = { v ->
                    onEvent(EditFoodItemEvent.SetProtein(0f))
                },
                onEvent = { v ->
                    onEvent(
                        EditFoodItemEvent.SetProtein(v)
                    )
                },
                initMacroString = state.protein.toString(),
            )

            EditMacroText(
                macroName = "Sugar",
                unit = state.unit.unit,
                onError = { v ->
                    onEvent(EditFoodItemEvent.SetSugar(0f))
                },
                onEvent = { v ->
                    onEvent(
                        EditFoodItemEvent.SetSugar(v)
                    )
                },
                initMacroString = state.sugar.toString(),
            )

            EditMacroText(
                macroName = "Fiber",
                unit = state.unit.unit,
                onError = { v ->
                    onEvent(EditFoodItemEvent.SetFiber(0f))
                },
                onEvent = { v ->
                    onEvent(
                        EditFoodItemEvent.SetFiber(v)
                    )
                },
                initMacroString = state.fiber.toString(),
            )

            Spacer(modifier = Modifier.height(10.dp))
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(0.95f),
                onClick = {
                    onEvent(EditFoodItemEvent.Save)
                    onBack()
                }
            ) {
                Text("Edit Save")
            }
        }
    }
}

@Composable
fun EditMacroText(
    macroName: String,
    unit: String,
    onError: (Float) -> Unit,
    onEvent: (Float) -> Unit,
    initMacroString: String,
) {
    var macroString by remember { mutableStateOf(initMacroString) }
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
            Text("$macroName ${if (unit == GENERIC.unit) "1" else "100"}${unit}")
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
fun EditFoodItem(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    foodId: Long,
    viewModel: EditFoodItemViewModel = viewModel(factory = EditFoodItemViewModel.Factory(foodId))
) {
    val state by viewModel.state.collectAsState()
    EditFoodItem(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun EditFoodItemPreview() {
    CaloryTrackerTheme {
        EditFoodItem(onBack = {}, state = EditFoodItemState(name = "Cavolo"), onEvent = {})
    }
}