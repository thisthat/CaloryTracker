package com.thisthatdc.calorytracker.components

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme


@Composable
fun FoodImage(modifier: Modifier = Modifier) {
    val image = "iVBORw0KGgoAAAANSUhEUgAAADQAAAA0CAIAAABKGoy8AAAAd0lEQVR4nOzQMQ3CYBhFUUJ+K4SJHRnM6EAJMw6YKqlO6uHmGzqcI+Dl5q3t97rM+d4+g2vXwa1x4ipxlbhKXCWuEleJq8RV4ipxlbhKXLX+j/vg3Ht/Dq6d+jlxlbhKXCWuEleJq8RV4ipxlbhKXCWuOgIAAP///AAFpxQ48swAAAAASUVORK5CYII="
    val ba = Base64.decode(image, Base64.DEFAULT)
    val bmp = BitmapFactory.decodeByteArray(ba, 0, ba.size)
    Image(
        modifier = modifier.padding(start = 20.dp),
        bitmap = bmp.asImageBitmap(),
        contentDescription = ""
    )
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_MASK)
@Composable
fun FoodImagePreview() {
    CaloryTrackerTheme(darkTheme = true) {
        Scaffold { innerPadding ->
            Row(
                modifier = Modifier.padding(innerPadding)
            ) {
                FoodImage()
            }
        }
    }
}