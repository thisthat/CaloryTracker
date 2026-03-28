package com.thisthatdc.calorytracker.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thisthatdc.calorytracker.components.Macro
import com.thisthatdc.calorytracker.components.MacroNutrient

@Composable
fun AllTab(modifier: Modifier = Modifier) {
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