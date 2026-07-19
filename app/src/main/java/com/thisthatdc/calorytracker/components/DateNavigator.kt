package com.thisthatdc.calorytracker.components

import android.content.res.Configuration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thisthatdc.calorytracker.data.home.ViewType
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme
import com.thisthatdc.calorytracker.util.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DateNavigator(
    modifier: Modifier = Modifier,
    day: Date,
    onReset: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    viewType: ViewType = ViewType.Day
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onPrev() }
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "prev")
        }
        val m = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = { onReset() }
            )
        }
        when (viewType) {
            ViewType.Day -> {
                if (Time.isToday(day)) {
                    Text("Today", modifier = m)
                } else {
                    val formatter: DateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
                    Text(formatter.format(day), modifier = m)
                }
            }

            ViewType.Week -> {
                val formatter: DateFormat = SimpleDateFormat("dd", Locale.US)
                val min = Date.from(Time.minusDays(day, 6))
                Text(buildString {
                    append(formatter.format(min))
                    append("-")
                    append(formatter.format(day))
                    append(" ")
                    append(SimpleDateFormat("MMM", Locale.US).format(day))
                }, modifier = m)

            }

            ViewType.Month -> {
                Text("Not yet implemented :)")
            }

            ViewType.Year -> {
                Text("Not yet implemented :)")
            }
        }

        IconButton(
            onClick = { onNext() }
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "next")
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_MASK)
@Composable
fun DateNavigatorPreview() {
    CaloryTrackerTheme(darkTheme = true) {
        Scaffold { innerPadding ->
            Row(
                modifier = Modifier.padding(innerPadding)
            ) {
                DateNavigator(
                    modifier = Modifier,
                    day = Date(),
                    onNext = {},
                    onPrev = {},
                    onReset = {},
                    viewType = ViewType.Week
                )
            }
        }
    }
}