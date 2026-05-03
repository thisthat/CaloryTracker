package com.thisthatdc.calorytracker.pages

import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.pinnedScrollBehavior
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thisthatdc.calorytracker.components.MacroNutrient
import com.thisthatdc.calorytracker.data.food.AddFoodMealEvent
import com.thisthatdc.calorytracker.data.settings.SettingsEvent
import com.thisthatdc.calorytracker.data.settings.SettingsState
import com.thisthatdc.calorytracker.data.settings.SettingsViewModel
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Settings(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory)
) {
    val state by viewModel.state.collectAsState()
    SettingsScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = pinnedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val uri = data?.data
        uri?.let {
            context.contentResolver.openOutputStream(it)?.let { outputStream ->
                scope.launch {
                    withContext(Dispatchers.IO) {
                        // TODO: serialize DB here
                        outputStream.write("hey".toByteArray())
                        outputStream.flush()
                        outputStream.close()

                    }
                }
            }
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Calory Tracker :: Settings")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onEvent(SettingsEvent.SaveSettings)
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
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Daily Calorie Goal",
                style = MaterialTheme.typography.titleLarge
            )

            MacroNutrient.entries.forEach { macro ->
                val limit: ClosedFloatingPointRange<Float> = when (macro) {
                    MacroNutrient.Calories -> 1000f..3000f
                    else -> 10f..100f
                }
                val value = when (macro) {
                    MacroNutrient.Calories -> state.calories
                    MacroNutrient.Protein -> state.protein
                    MacroNutrient.Fat -> state.fat
                    MacroNutrient.Carbs -> state.carbs
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = macro.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.widthIn(min = 80.dp)
                        )
                        Slider(
                            value = value.toFloat(),
                            onValueChange = { onEvent(handleEvent(macro,it.toInt())) },
                            valueRange = limit,
                            steps = 39,
                            colors = SliderDefaults.colors(
                                thumbColor = macro.color,
                                activeTrackColor = macro.color,
                            ),
                        )
                    }
                    Text(
                        text = "$value ${macro.unit}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.widthIn(min = 80.dp)
                    )
                }
            }

            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(0.95f),
                onClick = {
                    launcher.launch(createNewDocumentIntent())
                }
            ) {
                Text("Export DB")
            }
        }
    }
}

fun createNewDocumentIntent(): Intent {
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "html/txt"
        putExtra(Intent.EXTRA_TITLE, "test-${System.currentTimeMillis()}.txt")
    }
    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
    intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    return intent
}

fun handleEvent(macroNutrient: MacroNutrient, value: Int): SettingsEvent {
    return when (macroNutrient) {
        MacroNutrient.Calories -> {
            SettingsEvent.SetCalories(value)
        }
        MacroNutrient.Protein -> {
            SettingsEvent.SetProtein(value)
        }
        MacroNutrient.Fat -> {
            SettingsEvent.SetFat(value)
        }
        MacroNutrient.Carbs -> {
            SettingsEvent.SetCarbs(value)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    CaloryTrackerTheme {
        // Use SettingsScreen directly in Preview to avoid ViewModel instantiation issues
        SettingsScreen(
            state = SettingsState(calories = 2000),
            onEvent = {},
            onBack = {}
        )
    }
}
