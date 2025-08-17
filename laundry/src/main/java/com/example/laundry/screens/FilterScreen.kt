package com.example.laundry.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import com.project.common_utils.components.ReviewStars

@Composable
fun FilterScreen() {
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
                Text("< Back", color = Color(0xFFFF9800))
                Text("Filters", fontSize = 20.sp, color = Color.Black)
                Text("Clear", color = Color(0xFFFF9800))
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800), // Orange background
                        contentColor = Color.White // Text color
                    )
                ) {
                    Text("Apply",
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

            // Dropdown
            Text("Sort by", color = Color.Gray)
            Spacer(Modifier.height(8.dp))

            var selectedOption by remember { mutableStateOf("Recommend") }

            LargeDropdownMenu(
                options = listOf(
                    "Recommend",
                    "Price: Low to High",
                    "Price: High to Low",
                    "Newest First",
                    "Rating High to Low"
                ),
                selectedOption = selectedOption,
                onOptionSelected = { selectedOption = it },
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(Modifier.height(24.dp))

            // RangeSlider
            Text("Price/kg", color = Color.Gray)
            Spacer(Modifier.height(8.dp))
            var priceRange by remember { mutableStateOf(0f..30f) }
            RangeSlider(
                value = priceRange,
                onValueChange = { priceRange = it },
                valueRange = 0f..60f,
                steps = 4,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFFF9800),
                    activeTrackColor = Color(0xFFFF9800)
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(priceRange.start.toInt().toString())
                Text(priceRange.endInclusive.toInt().toString())
            }

            Spacer(Modifier.height(24.dp))

            // Placeholder for Rate
            Text("Rate", color = Color.Gray)
            ReviewStars(4.0f,{})
            Spacer(Modifier.height(8.dp))
            var selectedStars by remember { mutableStateOf(0) }
            // TODO: add star rating composable here
        }
    }
}


@Composable
fun LargeDropdownMenu(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        // Dropdown Trigger (Styled Box)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Gray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                )
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = selectedOption,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f) // takes all space except icon
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color.Gray
            )
        }

        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, fontSize = 16.sp) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun FilterScreenPreview() {
    FilterScreen()
}
