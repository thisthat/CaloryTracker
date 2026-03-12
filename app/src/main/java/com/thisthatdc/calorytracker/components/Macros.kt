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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme

enum class MacroNutrient(val color: Color, name: String) {
    Calories(Color(0xFF54a9fd), "Calories"),
    Protein(Color(0xFFa98dfe), "Protein"),
    Fat(Color(0xFFfc9c37), "Fat"),
    Carbs(Color(0xFF65d9e8), "Carbs"),
}

@Composable
fun Macros(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Macro(modifier, MacroNutrient.Calories)
        Macro(modifier, MacroNutrient.Protein)
        Macro(modifier, MacroNutrient.Fat)
        Macro(modifier, MacroNutrient.Carbs)
    }
}


@Composable
fun Macro(modifier: Modifier = Modifier, macro: MacroNutrient) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy((-10).dp)
    ) {
        Row(modifier = modifier.fillMaxWidth().padding(top=0.dp)) {
            Text(
                macro.name,
                fontSize = 14.sp,
            )
            Text(
                "17g remaining",
                fontSize =  10.sp,
                modifier = modifier.padding(start = 20.dp)
            )
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LinearProgressIndicator(
                progress = { 0.3f },
                modifier = modifier.fillMaxWidth(0.8f),
                color = macro.color,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                gapSize = 0.dp,
            )
            Text(
                "2.938/2.125 g",
                fontSize = 10.sp,
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
                Macros()
            }
        }
    }
}