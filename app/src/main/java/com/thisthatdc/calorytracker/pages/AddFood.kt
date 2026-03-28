package com.thisthatdc.calorytracker.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.pinnedScrollBehavior
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
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
import com.thisthatdc.calorytracker.tabs.AllTab
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFood(modifier: Modifier = Modifier) {
    val scrollBehavior = pinnedScrollBehavior(rememberTopAppBarState())
    var selectedDestination by rememberSaveable { mutableIntStateOf(0) }
    var textFieldState by rememberSaveable { mutableStateOf("") }
    var tabValue: Unit? by rememberSaveable { mutableStateOf(null) }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Find Food")
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
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
                onSearch = {  },
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
                Tab(
                    selected = selectedDestination == 0,
                    onClick = {
                        selectedDestination = 0
                    },
                    text = {
                        Text(
                            text = "All",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
                Tab(
                    selected = selectedDestination == 1,
                    onClick = {
                        selectedDestination = 1
                    },
                    text = {
                        Text(
                            text = "Favorite",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
                Tab(
                    selected = selectedDestination == 2,
                    onClick = {
                        selectedDestination = 2
                    },
                    text = {
                        Text(
                            text = "Recent",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }

            if (selectedDestination == 0) {
                AllTab()
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddFoodgPreview() {
    CaloryTrackerTheme {
        AddFood()
    }
}