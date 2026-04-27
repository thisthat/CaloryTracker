package com.thisthatdc.calorytracker.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thisthatdc.calorytracker.data.food.DefinedBy
import com.thisthatdc.calorytracker.data.food.Food
import com.thisthatdc.calorytracker.data.food.Unit
import com.thisthatdc.calorytracker.ui.theme.CaloriesColor
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme
import com.thisthatdc.calorytracker.ui.theme.CarbsColor
import com.thisthatdc.calorytracker.ui.theme.FatColor
import com.thisthatdc.calorytracker.ui.theme.ProteinColor

@Composable
fun SingleFood(modifier: Modifier = Modifier, food: Food, showQuantity: Boolean = true) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top=5.dp, bottom = 5.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = modifier.fillMaxWidth().padding(top=0.dp),
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
                    if (showQuantity) {
                        Text(
                            "30g • ",
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
            IconButton(
                modifier = modifier.weight(0.1f),
                onClick = { /* do something */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Localized description"
                )
            }
        }
    }
    HorizontalDivider(thickness = 2.dp)
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
                    food = Food(
                        uid = 0,
                        name = "Cavolo Rosso",
                        unit = Unit.GRAMS,
                        calories = 100,
                        carbs = 10,
                        fat = 10,
                        protein = 10,
                        sugar = 10,
                        fiber = 20,
                        definedBy = DefinedBy.USER,
                    )
                )
            }
        }
    }
}