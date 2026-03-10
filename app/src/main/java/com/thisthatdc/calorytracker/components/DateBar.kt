package com.thisthatdc.calorytracker.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme

@Composable
fun DateBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .height(50.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = {  }
        ) { Text("1d") }
        TextButton(
            onClick = {  }
        ) { Text("7d") }
        TextButton(
            onClick = {  }
        ) { Text("4w") }
        TextButton(
            onClick = { }
        ) { Text("1y") }
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