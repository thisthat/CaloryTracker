package com.thisthatdc.calorytracker.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thisthatdc.calorytracker.components.FoodList
import com.thisthatdc.calorytracker.components.Macro
import com.thisthatdc.calorytracker.components.MacroNutrient
import com.thisthatdc.calorytracker.components.Macros
import com.thisthatdc.calorytracker.components.SingleFood
import com.thisthatdc.calorytracker.pages.AddFood
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme

@Composable
fun AllTab(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        LazyColumn (modifier = modifier.fillMaxWidth().padding(start = 5.dp, end = 5.dp)) {
            item {
                SingleFood(modifier)
                SingleFood(modifier)
                SingleFood(modifier)
                SingleFood(modifier)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AllTabPreview() {
    CaloryTrackerTheme {
        AllTab()
    }
}