package com.thisthatdc.calorytracker.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thisthatdc.calorytracker.data.home.ViewType
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme

@Composable
fun DateBar(
    modifier: Modifier = Modifier,
    onClickDay: () -> Unit = {},
    onClickWeek: () -> Unit = {},
    onClickMonth: () -> Unit = {},
    onClickYear: () -> Unit = {},
    selected: ViewType = ViewType.Day
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selected == ViewType.Day) {
            FilledTonalButton(
                onClick = onClickDay,
            ) { Text("1d") }
        } else {
            TextButton(
                onClick = onClickDay,
            ) { Text("1d") }
        }
        if (selected == ViewType.Week) {
            FilledTonalButton(
                onClick = onClickWeek,
            ) { Text("7d") }
        } else {
            TextButton(
                onClick = onClickWeek
            ) { Text("7d") }
        }
        if (selected == ViewType.Month) {
            FilledTonalButton(
                onClick = onClickMonth,
            ) { Text("4w") }
        } else {
            TextButton(
                onClick = onClickMonth
            ) { Text("4w") }
        }

        if (selected == ViewType.Year) {
            FilledTonalButton(
                onClick = onClickYear,
            ) { Text("1y") }
        } else {
            TextButton(
                onClick = onClickYear
            ) { Text("1y") }
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_MASK)
@Composable
fun DateBarPreview() {
    CaloryTrackerTheme(darkTheme = true) {
        Scaffold() { innerPadding ->
            Row(
                modifier = Modifier.padding(innerPadding)
            ) {
                DateBar()
            }
        }
    }
}