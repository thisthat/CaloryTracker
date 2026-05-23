package com.thisthatdc.calorytracker.components

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thisthatdc.calorytracker.data.food.FoodState
import com.thisthatdc.calorytracker.data.food.Meals
import com.thisthatdc.calorytracker.data.food.Unit.GENERIC
import com.thisthatdc.calorytracker.data.home.HomeState
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme
import kotlin.math.ceil

@Composable
fun FoodList(
    modifier: Modifier = Modifier,
    onFoodClick: (Meals, Long) -> Unit,
    onDeleteFood: (Long) -> Unit,
    state: HomeState
) {
    val breakfast = state.food.filter { it.meal == Meals.Breakfast }
    val lunch = state.food.filter { it.meal == Meals.Lunch }
    val snacks = state.food.filter { it.meal == Meals.Snacks }
    val dinner = state.food.filter { it.meal == Meals.Dinner }
    val day = state.day.toInstant().toEpochMilli()
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Meal(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer),
            meal = Meals.Breakfast,
            onFoodClick = onFoodClick,
            food = breakfast,
            day = day,
            onDeleteFood = onDeleteFood,
        )
        Meal(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer),
            meal = Meals.Lunch,
            onFoodClick = onFoodClick,
            food = lunch,
            day = day,
            onDeleteFood = onDeleteFood,
        )
        Meal(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer),
            meal = Meals.Snacks,
            onFoodClick = onFoodClick,
            food = snacks,
            day = day,
            onDeleteFood = onDeleteFood,
        )
        Meal(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer),
            meal = Meals.Dinner,
            onFoodClick = onFoodClick,
            food = dinner,
            day = day,
            onDeleteFood = onDeleteFood,
        )
    }
}


@Composable
fun Meal(
    modifier: Modifier = Modifier,
    meal: Meals,
    food: List<FoodState>,
    day: Long,
    onFoodClick: (Meals, Long) -> Unit,
    onDeleteFood: (Long) -> Unit,
) {
    var totalCalories = 0
    var totalFat = 0
    var totalProtein = 0
    var totalCarbs = 0
    food.forEach { food ->
        val ratio = if(food.unit == GENERIC) food.quantity / 1f else food.quantity / 100f
        totalCalories += ceil((ratio * food.calories).toDouble()).toInt()
        totalFat += ceil((ratio * food.fat).toDouble()).toInt()
        totalProtein += ceil((ratio * food.protein).toDouble()).toInt()
        totalCarbs += ceil((ratio * food.carbs).toDouble()).toInt()
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(text = meal.name, modifier = modifier.padding(start = 10.dp, top = 10.dp))
        MacroList(
            modifier = modifier, state = MacroListState(
                caloriesStatus = "$totalCalories",
                proteinStatus = "$totalProtein",
                fatStatus = "$totalFat",
                carbsStatus = "$totalCarbs"
            )
        )
        HorizontalDivider(thickness = 1.dp)
        for (f in food) {
            SingleFood(modifier = Modifier, food = f, onDeleteFood = onDeleteFood)
            HorizontalDivider(thickness = 1.dp)
        }
        TextButton(
            onClick = { onFoodClick(meal, day) }
        ) {
            Text("Add Food")
        }
        HorizontalDivider(thickness = 1.dp, modifier = modifier)

    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_MASK)
@Composable
fun FoodListPreview() {
    CaloryTrackerTheme(darkTheme = true) {
        Scaffold { innerPadding ->
            Row(
                modifier = Modifier.padding(innerPadding)
            ) {
                FoodList(onFoodClick = { _: Meals, _: Long -> }, state = HomeState(), onDeleteFood = {})
            }
        }
    }
}