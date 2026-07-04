package com.thisthatdc.calorytracker.pages

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme
import com.thisthatdc.charting.ColumnChart
import com.thisthatdc.charting.models.BarProperties
import com.thisthatdc.charting.models.Bars
import com.thisthatdc.charting.models.DividerProperties
import com.thisthatdc.charting.models.GridProperties
import com.thisthatdc.charting.models.HorizontalIndicatorProperties
import com.thisthatdc.charting.models.IndicatorCount
import com.thisthatdc.charting.models.IndicatorPosition
import com.thisthatdc.charting.models.LabelHelperProperties
import com.thisthatdc.charting.models.LabelProperties

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
        ColumnChart(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(innerPadding),
            data = remember {
                listOf(
                    Bars(
                        label = "Jan",
                        values = listOf(
                            Bars.Data(
                                label = "Linux",
                                value = 50.0,
                                color = Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF0099FF),
                                        Color(0xFFEC407A),
                                    )
                                )
                            ),
                            Bars.Data(
                                label = "Windows",
                                value = 70.0,
                                color = SolidColor(Color.Red)
                            )
                        ),
                    ),
                    Bars(
                        label = "Feb",
                        values = listOf(
                            Bars.Data(
                                label = "Linux",
                                value = 80.0,
                                color = Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF0099FF),
                                        Color(0xFFEC407A),
                                    )
                                )
                            ),
                            Bars.Data(
                                label = "Windows",
                                value = 60.0,
                                color = SolidColor(Color.Red)
                            )
                        ),
                    )
                )
            },
            barProperties = BarProperties(
                cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
                spacing = 30.dp,
                thickness = 10.dp
            ),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            labelProperties = LabelProperties(
                enabled = true,
                textStyle = MaterialTheme.typography.labelSmall.copy(color=Color.White),
            ),
            labelHelperProperties = LabelHelperProperties(
                textStyle = MaterialTheme.typography.labelSmall.copy(color=Color.White)
            ),
            gridProperties = GridProperties(enabled = false),
            indicatorProperties = HorizontalIndicatorProperties(
                enabled = true,
                count = IndicatorCount.CountBased(count = 3),
                position = IndicatorPosition.Horizontal.Start,
                padding = 1.dp,
                textStyle = TextStyle.Default.copy(Color.Green)
            )
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