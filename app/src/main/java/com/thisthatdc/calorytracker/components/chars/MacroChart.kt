package com.thisthatdc.calorytracker.components.chars

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme
import com.thisthatdc.charting.ColumnChart
import com.thisthatdc.charting.models.BarProperties
import com.thisthatdc.charting.models.Bars
import com.thisthatdc.charting.models.GridProperties
import com.thisthatdc.charting.models.HorizontalIndicatorProperties
import com.thisthatdc.charting.models.IndicatorCount
import com.thisthatdc.charting.models.IndicatorPosition
import com.thisthatdc.charting.models.LabelHelperProperties
import com.thisthatdc.charting.models.LabelProperties

@Composable
fun MacroChart(
    modifier: Modifier = Modifier,
    viewModel: MacroChartViewModel = viewModel(factory = MacroChartViewModel.Factory)
) {
    val state by viewModel.state.collectAsState()
    MacroChart(
        modifier,
        state,
        onCaloriesClick = { viewModel.onEvent(MacroChartEvent.ChangeMacro(FilterType.Calories)) },
        onProteinClick = { viewModel.onEvent(MacroChartEvent.ChangeMacro(FilterType.Protein)) },
        onFatClick = { viewModel.onEvent(MacroChartEvent.ChangeMacro(FilterType.Fat)) },
        onCarbsClick = { viewModel.onEvent(MacroChartEvent.ChangeMacro(FilterType.Carb)) },
    )
}

@Composable
fun MacroChart(
    modifier: Modifier = Modifier,
    state: MacroChartState,
    onCaloriesClick: () -> Unit = {},
    onProteinClick: () -> Unit = {},
    onFatClick: () -> Unit = {},
    onCarbsClick: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Charting(modifier.fillMaxHeight(0.5f), listOf())
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.filterType == FilterType.Calories) {
                FilledTonalButton(onClick = { onCaloriesClick() }) {
                    Text("Calories")
                }
            } else {
                OutlinedButton(onClick = { onCaloriesClick() }) {
                    Text("Calories")
                }
            }
            if (state.filterType == FilterType.Protein) {
                FilledTonalButton(onClick = { onProteinClick() }) {
                    Text("Protein")
                }
            } else {
                OutlinedButton(onClick = { onProteinClick() }) {
                    Text("Protein")
                }
            }
            if (state.filterType == FilterType.Fat) {
                FilledTonalButton(onClick = { onFatClick() }) {
                    Text("Fat")
                }
            } else {
                OutlinedButton(onClick = { onFatClick() }) {
                    Text("Fat")
                }
            }
            if (state.filterType == FilterType.Carb) {
                FilledTonalButton(onClick = { onCarbsClick() }) {
                    Text("Carbs")
                }
            } else {
                OutlinedButton(onClick = { onCarbsClick() }) {
                    Text("Carbs")
                }
            }
        }
    }
}


@Composable
fun Charting(modifier: Modifier = Modifier, data: List<Bars>) {
    return ColumnChart(
        modifier = modifier,
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
                                    Color(0xFFE9F9F9),
                                )
                            )
                        ),
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
            textStyle = MaterialTheme.typography.labelSmall.copy(color = Color.White),
        ),
        labelHelperProperties = LabelHelperProperties(
            textStyle = MaterialTheme.typography.labelSmall.copy(color = Color.White)
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


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CaloryTrackerTheme {
        MacroChart(modifier = Modifier, state = MacroChartState())
    }
}