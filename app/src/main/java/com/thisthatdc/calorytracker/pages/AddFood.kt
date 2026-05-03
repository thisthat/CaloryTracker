package com.thisthatdc.calorytracker.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.pinnedScrollBehavior
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thisthatdc.calorytracker.data.food.AddFoodViewModel
import com.thisthatdc.calorytracker.data.food.Food
import com.thisthatdc.calorytracker.data.food.FoodExample
import com.thisthatdc.calorytracker.data.food.Meals
import com.thisthatdc.calorytracker.tabs.AllTab
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme

enum class Tabs(
    val title: String,
    val component: @Composable (List<Food>, (Long) -> Unit) -> Unit
) {
    ALL(title = "All", component = ::AllTab),
    YOUR_FOOD(title = "Your food", component = ::AllTab),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFood(
    modifier: Modifier = Modifier,
    meal: Meals,
    onBack: () -> Unit,
    onFoodItemClick: () -> Unit,
    onFoodSelected: (Long) -> Unit,
    foods: List<Food>,
    userDefinedFood: List<Food>,
) {
    val scrollBehavior = pinnedScrollBehavior(rememberTopAppBarState())
    var selectedDestination by rememberSaveable { mutableIntStateOf(0) }
    var textFieldState by rememberSaveable { mutableStateOf("") }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Find ${meal.name} Food")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onFoodItemClick() }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputField(
                modifier = Modifier.fillMaxWidth(),
                query = textFieldState,
                onQueryChange = { textFieldState = it },
                onSearch = { },
                expanded = false,
                onExpandedChange = {},
                enabled = true,
                placeholder = { Text("Search for your food") },
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = "search") },
            )

            PrimaryTabRow(
                selectedTabIndex = selectedDestination,
                modifier = modifier
            ) {
                Tabs.entries.forEachIndexed { index, elm ->
                    Tab(
                        selected = selectedDestination == index,
                        onClick = {
                            selectedDestination = index
                        },
                        text = {
                            Text(
                                text = elm.title,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            Tabs.entries.forEachIndexed { index, elm ->
                if (selectedDestination == index) {
                    elm.component(if (index == 0) foods else userDefinedFood, onFoodSelected)
                }
            }
        }
    }
}

@Composable
fun AddFood(
    modifier: Modifier = Modifier,
    meal: Meals,
    onBack: () -> Unit,
    onFoodItemClick: () -> Unit,
    onFoodSelected: (Long) -> Unit,
    viewModel: AddFoodViewModel = viewModel(factory = AddFoodViewModel.Factory)
) {
    val foods by viewModel.foods.collectAsState()
    val userDefinedFood by viewModel.userDefinedFood.collectAsState()
    AddFood(
        foods = foods,
        userDefinedFood = userDefinedFood,
        //onEvent = viewModel::onEvent,
        onBack = onBack,
        modifier = modifier,
        meal = meal,
        onFoodItemClick = onFoodItemClick,
        onFoodSelected = onFoodSelected,
    )
}


@Preview(showBackground = true)
@Composable
fun AddFoodPreview() {
    CaloryTrackerTheme {
        AddFood(
            meal = Meals.Breakfast,
            onBack = {},
            onFoodItemClick = {},
            onFoodSelected = {},
            foods = FoodExample,
            userDefinedFood = FoodExample
        )
    }
}