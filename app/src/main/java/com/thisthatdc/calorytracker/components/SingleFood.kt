package com.thisthatdc.calorytracker.components

import android.content.res.Configuration
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thisthatdc.calorytracker.data.food.Food
import com.thisthatdc.calorytracker.data.food.FoodExample
import com.thisthatdc.calorytracker.data.food.FoodState
import com.thisthatdc.calorytracker.data.food.Unit.GENERIC
import com.thisthatdc.calorytracker.ui.theme.CaloriesColor
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme
import com.thisthatdc.calorytracker.ui.theme.CarbsColor
import com.thisthatdc.calorytracker.ui.theme.FatColor
import com.thisthatdc.calorytracker.ui.theme.ProteinColor
import kotlin.math.ceil


private val EMPTY: (Long) -> Unit = {}

@Composable
fun SingleFood(
    modifier: Modifier = Modifier,
    food: Food,
    quantity: Float = 0f,
    onAddFood: (Long) -> Unit = EMPTY,
    onDeleteFood: (Long) -> Unit = EMPTY,
    onEdit: (Long) -> Unit = EMPTY
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {onAddFood(food.uid)},
                onLongClick = {expanded = !expanded}
            )
            .padding(top = 5.dp, bottom = 5.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            FoodImage(
                modifier = modifier
            )
            Column(
                verticalArrangement = Arrangement.spacedBy((-10).dp),
                modifier = modifier.weight(0.8f)
            ) {
                Text(
                    food.name,
                    fontSize = 10.sp,
                    modifier = modifier.padding(start = 20.dp)
                )
                Row(modifier = modifier.padding(start = 20.dp)) {
                    // quantity
                    if (quantity > 0) {
                        Text(
                            "${quantity}${food.unit.unit} • ",
                            fontSize = 10.sp,
                        )
                    }
                    Text(
                        "${food.calories}Cal",
                        fontSize = 10.sp,
                        color = CaloriesColor
                    )
                    Text(
                        " • ",
                        fontSize = 10.sp,
                    )
                    Text(
                        "${food.protein}P",
                        fontSize = 10.sp,
                        color = ProteinColor
                    )
                    Text(
                        " • ",
                        fontSize = 10.sp,
                    )
                    Text(
                        "${food.fat}F",
                        fontSize = 10.sp,
                        color = FatColor
                    )
                    Text(
                        " • ",
                        fontSize = 10.sp,
                    )
                    Text(
                        "${food.carbs}C",
                        fontSize = 10.sp,
                        color = CarbsColor
                    )
                }
            }
            if (quantity == 0f) {
                IconButton(
                    modifier = modifier.weight(0.1f),
                    onClick = { onAddFood(food.uid) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Localized description"
                    )
                }
            } else {
                IconButton(
                    modifier = modifier.weight(0.1f),
                    onClick = { onDeleteFood(food.uid) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Localized description"
                    )
                }
            }
        }
        if(onEdit !== EMPTY) {
            Box(contentAlignment = Alignment.TopEnd) {
                DropdownMenu(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .align(Alignment.Center),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            expanded = false
                            onEdit(food.uid)
                        }
                    )
                }
            }
        }

    }
    HorizontalDivider(thickness = 2.dp)
}

fun scale(v: Float): Float {
    return ceil(v.toDouble() * 1000).toFloat() / 1000f
}

@Composable
fun SingleFood(modifier: Modifier = Modifier, food: FoodState, onDeleteFood: (Long) -> Unit) {
    val ratio = if(food.unit == GENERIC) food.quantity / 1f else food.quantity / 100f
    val f = Food(
        uid = food.uid,
        name = food.name,
        unit = food.unit,
        calories = ceil((food.calories * ratio).toDouble()).toInt(),
        carbs = scale(food.carbs * ratio),
        fat = scale(food.fat * ratio),
        protein = scale(food.protein * ratio),
        sugar = scale(food.sugar * ratio),
        fiber = scale(food.fiber * ratio),
        definedBy = food.definedBy,
    )
    SingleFood(
        modifier = modifier,
        food = f,
        quantity = food.quantity,
        onAddFood = {},
        onDeleteFood = onDeleteFood,
    )
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_MASK)
@Composable
fun SingleFoodPreview() {
    CaloryTrackerTheme(darkTheme = true) {
        Scaffold { innerPadding ->
            Row(
                modifier = Modifier.padding(innerPadding)
            ) {
                SingleFood(
                    food = FoodExample[0],
                )
            }
        }
    }
}