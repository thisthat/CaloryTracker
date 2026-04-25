package com.thisthatdc.calorytracker.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.pinnedScrollBehavior
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thisthatdc.calorytracker.ui.theme.CaloryTrackerTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodItem(modifier: Modifier = Modifier, onBack: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    val scrollBehavior = pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Add new Food")
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
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = "",
                placeholder = { Text("Name of your food")},
                onValueChange = {},
                label = {
                    Text("Name")
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f).clickable {
                    expanded = true
                },
                value = "g",
                readOnly = true,
                enabled = false,
                onValueChange = {},
                label = {
                    Text("Unit")
                },
                trailingIcon = { Icon(Icons.Outlined.ArrowDropDown, contentDescription = null)}
            )
            Box(contentAlignment = Alignment.TopStart) {
                DropdownMenu(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("g") },
                        leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                        onClick = { /* Do something... */ }
                    )
                    DropdownMenuItem(
                        text = { Text("ml") },
                        leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                        onClick = { /* Do something... */ }
                    )
                }
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = "",
                onValueChange = {},
                label = {
                    Text("Calories 100g")
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = "",
                onValueChange = {},
                label = {
                    Text("Carbs 100g")
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = "",
                onValueChange = {},
                label = {
                    Text("Fat 100g")
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = "",
                onValueChange = {},
                label = {
                    Text("Protein 100g")
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = "",
                onValueChange = {},
                label = {
                    Text("Sugar 100g")
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.95f),
                value = "",
                onValueChange = {},
                label = {
                    Text("Fiber 100g")
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(0.95f),
                onClick = {  }
            ) {
                Text("Save")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddFoodItemPreview() {
    CaloryTrackerTheme {
        AddFoodItem(onBack = {})
    }
}