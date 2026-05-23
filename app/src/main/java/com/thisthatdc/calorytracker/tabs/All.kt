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
import com.thisthatdc.calorytracker.data.food.Food
import com.thisthatdc.calorytracker.data.food.FoodExample
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme

@Composable
fun AllTab(
    modifier: Modifier = Modifier,
    food: List<Food>,
    onAddFood: (Long) -> Unit,
    onEdit: (Long) -> Unit,
    searchWord: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp)
        ) {
            items(food) { f ->
                if (searchWord.isEmpty() || f.name.lowercase().contains(searchWord.lowercase())) {
                    SingleFood(modifier = modifier, food = f, onAddFood = onAddFood, onEdit = onEdit)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AllTabPreview() {
    CaloryTrackerTheme {
        AllTab(food = FoodExample, onAddFood = {}, onEdit = {}, searchWord = "")
    }
}