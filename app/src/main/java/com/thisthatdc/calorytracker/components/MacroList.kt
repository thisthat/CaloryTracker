package com.thisthatdc.calorytracker.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme


data class MacroListState(
    val caloriesStatus: String,
    val proteinStatus: String,
    val fatStatus: String,
    val carbsStatus: String,
)

@Composable
fun MacroList(modifier: Modifier = Modifier, state: MacroListState) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MacroMeal(modifier = modifier, macro = MacroNutrient.Calories, state.caloriesStatus)
        MacroMeal(modifier = modifier, macro = MacroNutrient.Protein, state.proteinStatus)
        MacroMeal(modifier = modifier, macro = MacroNutrient.Fat, state.fatStatus)
        MacroMeal(modifier = modifier, macro = MacroNutrient.Carbs, state.carbsStatus)
    }
}

@Composable
fun MacroMeal(modifier: Modifier = Modifier, macro: MacroNutrient, value: String) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = modifier.size(8.dp), onDraw = {
            drawCircle(
                color = macro.color,
                center = Offset(size.width / 2f, size.height / 2f)
            )
        })
        Text(
            "$value ${macro.unit}",
            modifier = modifier.padding(start = 5.dp),
            fontSize = 10.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MacroListPreview() {
    CaloryTrackerTheme {
        MacroList(state = MacroListState(
            caloriesStatus = "1",
            proteinStatus = "12",
            fatStatus = "100",
            carbsStatus = "200"
        ))
    }
}