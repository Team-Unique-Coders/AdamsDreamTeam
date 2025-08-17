package com.example.laundry.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laundry.navigation.LaundryDestinations
import com.project.common_utils.components.RangeSliderComponent

@Composable
fun YourLaundryScreen(
    onOpen: (String) -> Unit = {},
    onBack: () -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Your Laundry", fontSize = 20.sp, color = Color.Black)
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        onOpen(LaundryDestinations.ORDERLAUNDRY)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800), // Orange background
                        contentColor = Color.White // Text color
                    )
                ) {
                    Text("Next",
                        modifier = Modifier.padding(70.dp,0.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)

        ) {
            Spacer(Modifier.height(16.dp))

            var priceRange by remember { mutableStateOf(0f..10f) }
            RangeSliderComponent(
                heading = "Laundry/kg",
                range = priceRange,
                onRangeChange = { priceRange = it },
                valueRange = 0f..50f,
                steps = 1
            )

            Spacer(Modifier.height(24.dp))
            // Dropdown
            Text("Dry", color = Color.Gray)
            Spacer(Modifier.height(8.dp))

            var selectedOption by remember { mutableStateOf("1") }

            LargeDropdownMenu(
                options = listOf(
                    "1",
                    "2",
                    "3",
                    "4",
                    "5"
                ),
                selectedOption = selectedOption,
                onOptionSelected = { selectedOption = it },
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(Modifier.height(24.dp))

            IroningCheckbox()

            Spacer(Modifier.height(24.dp))
            var availability by remember { mutableStateOf(0f..10f) }
            RangeSliderComponent(
                heading = "Availability",
                range = availability,
                onRangeChange = { availability = it },
                valueRange = 0f..50f,
                steps = 5
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun IroningCheckbox() {
    var ironing by remember { mutableStateOf("Yes") } // default selection

    Text("Ironing", color = Color.Gray)
    Spacer(Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = ironing == "Yes",
                onCheckedChange = { if (it) ironing = "Yes" },
                colors = androidx.compose.material3.CheckboxDefaults.colors(
                    checkedColor = Color(0xFFFF9800),       // Thumb/box color when checked
                    uncheckedColor = Color.Gray,            // Box color when unchecked
                    checkmarkColor = Color.White            // Tick mark color
                )
            )
            Spacer(Modifier.width(4.dp))
            Text("Yes")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = ironing == "No",
                onCheckedChange = { if (it) ironing = "No" },
                colors = androidx.compose.material3.CheckboxDefaults.colors(
                    checkedColor = Color(0xFFFF9800),       // Thumb/box color when checked
                    uncheckedColor = Color.Gray,            // Box color when unchecked
                    checkmarkColor = Color.White            // Tick mark color
                )

            )
            Spacer(Modifier.width(4.dp))
            Text("No")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun YourLaundryScreenPreview() {
    YourLaundryScreen()
}