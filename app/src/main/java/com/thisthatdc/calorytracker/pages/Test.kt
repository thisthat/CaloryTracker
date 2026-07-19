package com.thisthatdc.calorytracker.pages

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.pinnedScrollBehavior
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.thisthatdc.calorytracker.components.chars.MacroChart
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Test(
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = pinnedScrollBehavior(rememberTopAppBarState())

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                ),
                title = {
                    Text("Calories Tracker")
                },
                actions = {},
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        MacroChart(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(innerPadding),
            day = Date(),
        )
    }
}


@Preview(showBackground = true)
@Composable
fun TestPreview() {
    CaloryTrackerTheme {
        Test()
    }
}