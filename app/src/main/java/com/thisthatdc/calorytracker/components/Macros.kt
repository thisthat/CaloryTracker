package com.thisthatdc.calorytracker.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme

enum class MacroNutrient(val color: Color, val title: String, val unit: String = "g") {
    Calories(Color(0xFF54a9fd), "Calories", "kcal"),
    Protein(Color(0xFFa98dfe), "Protein", "g"),
    Fat(Color(0xFFfc9c37), "Fat", "g"),
    Carbs(Color(0xFF65d9e8), "Carbs", "g"),
}

data class MacroState(
    val currentCalories: Int = 10,
    val maxCalories: Int = 1300,
    val currentFat: Int = 25,
    val maxFat: Int = 50,
    val currentProtein: Int = 25,
    val maxProtein: Int = 100,
    val currentCarbs: Int = 99,
    val maxCarbs: Int = 100
)

data class MacroStateItem(
    val currentVal: Int = 1300,
    val maxVal: Int = 1300,
)

@Composable
fun Macros(modifier: Modifier = Modifier, state: MacroState) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Macro(
            modifier,
            MacroNutrient.Calories,
            state = MacroStateItem(currentVal = state.currentCalories, maxVal = state.maxCalories)
        )
        Macro(
            modifier,
            MacroNutrient.Protein,
            state = MacroStateItem(currentVal = state.currentProtein, maxVal = state.maxProtein)
        )
        Macro(
            modifier,
            MacroNutrient.Fat,
            state = MacroStateItem(currentVal = state.currentFat, maxVal = state.maxFat)
        )
        Macro(
            modifier,
            MacroNutrient.Carbs,
            state = MacroStateItem(currentVal = state.currentCarbs, maxVal = state.maxCarbs)
        )
    }
}


@Composable
fun Macro(modifier: Modifier = Modifier, macro: MacroNutrient, state: MacroStateItem) {
    val remaining = state.maxVal - state.currentVal
    val progress = state.currentVal.toFloat() / state.maxVal.toFloat()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy((-10).dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 0.dp)
        ) {
            Text(
                macro.name,
                fontSize = 14.sp,
            )
            Text(
                "${remaining}${macro.unit} remaining",
                fontSize = 10.sp,
                modifier = modifier.padding(start = 20.dp)
            )
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = modifier.fillMaxWidth(0.8f),
                color = macro.color,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                gapSize = 0.dp,
            )
            Text(
                "${state.currentVal}/${state.maxVal} ${macro.unit}",
                fontSize = 8.sp,
                textAlign = TextAlign.Right,
                modifier = modifier
                    .fillMaxWidth(1f)
                    .padding(start = 5.dp)
            )
        }

    }

}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_MASK)
@Composable
fun MacrosPreview() {
    CaloryTrackerTheme(darkTheme = true) {
        Scaffold() { innerPadding ->
            Row(
                modifier = Modifier.padding(innerPadding)
            ) {
                Macros(
                    state = MacroState()
                )
            }
        }
    }
}