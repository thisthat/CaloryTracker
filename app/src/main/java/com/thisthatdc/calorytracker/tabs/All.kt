package com.thisthatdc.calorytracker.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thisthatdc.calorytracker.components.SingleFood
import com.thisthatdc.calorytracker.data.food.DefinedBy
import com.thisthatdc.calorytracker.data.food.Food
import com.thisthatdc.calorytracker.data.food.Unit
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme

@Composable
fun AllTab(food: List<Food>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        LazyColumn(modifier = modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp)) {
            items(food) { f ->
                SingleFood(modifier, f, false)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AllTabPreview() {
    CaloryTrackerTheme {
        AllTab(food = listOf(
            Food(
                uid = 0,
                name = "Cavolo Rosso",
                calories = 100,
                carbs = 10,
                fat = 10,
                protein = 10,
                unit = Unit.LIQUID,
                sugar = 1,
                fiber = 1,
                definedBy = DefinedBy.USER,
            )
        )
        )
    }
}