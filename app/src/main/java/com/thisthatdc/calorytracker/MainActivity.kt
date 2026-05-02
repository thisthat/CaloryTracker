package com.thisthatdc.calorytracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import com.thisthatdc.calorytracker.pages.AddFood
import com.thisthatdc.calorytracker.pages.Home
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme
import com.thisthatdc.calorytracker.util.Time
import java.time.Clock
import java.time.temporal.TemporalField

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaloryTrackerTheme {
//                Home()
//                AddFood()
                NavigationRoot(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}