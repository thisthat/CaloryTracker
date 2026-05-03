package com.thisthatdc.calorytracker.components

import android.content.res.Configuration
import android.util.Log
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
import com.thisthatdc.calorytracker.ui.theme.CaloriesColor
import com.thisthatdc.calorytracker.ui.theme.CaloriesOverColor
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme
import com.thisthatdc.calorytracker.ui.theme.CarbsColor
import com.thisthatdc.calorytracker.ui.theme.CarbsOverColor
import com.thisthatdc.calorytracker.ui.theme.FatColor
import com.thisthatdc.calorytracker.ui.theme.FatOverColor
import com.thisthatdc.calorytracker.ui.theme.ProteinColor
import com.thisthatdc.calorytracker.ui.theme.ProteinOverColor
import kotlin.math.abs

enum class MacroNutrient(
    val color: Color,
    val overColor: Color,
    val title: String,
    val unit: String = "g"
) {
    Calories(CaloriesColor, CaloriesOverColor, "Calories", "kcal"),
    Protein(ProteinColor, ProteinOverColor, "Protein", "g"),
    Fat(FatColor, FatOverColor, "Fat", "g"),
    Carbs(CarbsColor, CarbsOverColor, "Carbs", "g"),
}

data class MacroState(
    val currentCalories: Int = 0,
    val maxCalories: Int = 1300,
    val currentFat: Int = 0,
    val maxFat: Int = 50,
    val currentProtein: Int = 0,
    val maxProtein: Int = 100,
    val currentCarbs: Int = 0,
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
    Log.d("Macros", "progress: $progress")
    val isOver = remaining < 0
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
            var msg = "${remaining}${macro.unit} remaining"
            if (isOver) {
                msg = "${abs(remaining)}${macro.unit} over"
            }
            Text(
                msg,
                fontSize = 10.sp,
                modifier = modifier.padding(start = 20.dp)
            )

        }
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (isOver) {
                val p = 1 - (progress % 1)
                Log.d("Progress", "New Progress $p")
                LinearProgressIndicator(
                    progress = { p },
                    modifier = modifier.fillMaxWidth(0.8f),
                    color = macro.color,
                    trackColor = macro.overColor,
                    gapSize = 0.dp,
                )
            } else {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = modifier.fillMaxWidth(0.8f),
                    color = macro.color,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    gapSize = 0.dp,
                )
            }
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
        Scaffold { innerPadding ->
            Row(
                modifier = Modifier.padding(innerPadding)
            ) {
                Macros(
                    state = MacroState(
                        currentCalories = 120,
                        currentProtein = 120,
                        currentFat = 120,
                        currentCarbs = 120,
                        maxCalories = 100,
                        maxProtein = 100,
                        maxFat = 100,
                        maxCarbs = 100,
                    )
                )
            }
        }
    }
}