package com.thisthatdc.calorytracker.components

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thisthatdc.calorytracker.data.food.Meals
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme

@Composable
fun FoodList(modifier: Modifier = Modifier, onFoodClick: (Meals) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Meal(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer),
            meal = Meals.Breakfast,
            onFoodClick = onFoodClick,
        )
        Meal(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer),
            meal = Meals.Lunch,
            onFoodClick = onFoodClick,
        )
        Meal(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer),
            meal = Meals.Snacks,
            onFoodClick = onFoodClick,
        )
        Meal(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer),
            meal = Meals.Dinner,
            onFoodClick = onFoodClick,
        )
    }
}


@Composable
fun MacroMeal(modifier: Modifier = Modifier, macro: MacroNutrient) {
    Canvas(modifier = modifier.size(8.dp), onDraw = {
        drawCircle(
            color = macro.color,
            center = Offset(size.width / 2f, size.height / 2f)
        )
    })
    Text(
        "227 / 312 Cal",
        fontSize = 10.sp,
    )
}


@Composable
fun Meal(modifier: Modifier = Modifier, meal: Meals, onFoodClick: (Meals) -> Unit) {
    var items = arrayListOf<String>();
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(text = meal.name, modifier = modifier.padding(start = 10.dp, top = 10.dp))
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MacroMeal(modifier = modifier, macro = MacroNutrient.Calories)
            MacroMeal(modifier = modifier, macro = MacroNutrient.Protein)
            MacroMeal(modifier = modifier, macro = MacroNutrient.Fat)
            MacroMeal(modifier = modifier, macro = MacroNutrient.Carbs)
        }
        HorizontalDivider(thickness = 1.dp)
        for (item in items) {
            Text(text = item)
            HorizontalDivider(thickness = 1.dp)
        }
        TextButton(
            onClick = { onFoodClick(meal) }
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
                FoodList(onFoodClick = {})
            }
        }
    }
}