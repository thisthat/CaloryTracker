package com.thisthatdc.calorytracker.components

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme

@Composable
fun SingleFood(modifier: Modifier = Modifier) {
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
                verticalArrangement = Arrangement.spacedBy(0.dp),
                modifier = modifier.weight(0.8f)
            ) {
                Text(
                    "Cavolo Rosso",
                    fontSize = 10.sp,
                    modifier = modifier.padding(start = 20.dp)
                )
                Text(
                    "30g • 9 Cal • 1 P • 2 F • 3 C",
                    fontSize = 10.sp,
                    modifier = modifier.padding(start = 20.dp)
                )
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
                SingleFood()
            }
        }
    }
}