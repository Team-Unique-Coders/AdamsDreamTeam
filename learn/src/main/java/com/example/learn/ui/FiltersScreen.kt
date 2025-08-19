package com.example.learn.ui

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
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.common_utils.R
import com.project.common_utils.ThreeValueSlider
import com.project.common_utils.components.BackArrowIcon
import com.project.common_utils.components.OrangeButton
import com.project.common_utils.components.ReviewStars

@Composable
fun LearnFiltersScreen(
    onBack: () -> Unit,
    onApply: (FiltersState) -> Unit
) {
    var price by remember { mutableStateOf(15f) }        // $/h
    var rating by remember { mutableStateOf(4.0f) }      // stars
    var distance by remember { mutableStateOf(500f) }    // meters

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Top bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BackArrowIcon(onClick = onBack)
            Spacer(Modifier.weight(1f))
            Text(
                "Filters",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.weight(1f))
            Text("Clear", color = colorResource(R.color.orange))
        }

        Spacer(Modifier.height(16.dp))

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

        Spacer(Modifier.height(16.dp))

        Spacer(Modifier.height(16.dp))

        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Price/hr", color = Color.Gray)
            Spacer(Modifier.width(16.dp))
            HorizontalDivider()
        }
        Spacer(Modifier.height(8.dp))
        var priceRange by remember { mutableStateOf(0f..30f) }
        RangeSlider(
            value = priceRange,
            onValueChange = { priceRange = it },
            valueRange = 0f..60f,
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

        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Rate")
            Spacer(Modifier.width(16.dp))
            HorizontalDivider()
        }
        ReviewStars(rating = rating, onRatingChange = { rating = it }, size = 28.dp)
        Spacer(Modifier.weight(1f))

        OrangeButton(
            onClick = {
                onApply(
                    FiltersState(
                        pricePerHourMax = price.toInt(),
                        minRating = rating,
                        maxDistanceMeters = distance.toInt(),
                        categories = buildList {
                        }
                    )
                )
            },
            text = "Apply filters"
        )
    }
}

@Composable
private fun FilterToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
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