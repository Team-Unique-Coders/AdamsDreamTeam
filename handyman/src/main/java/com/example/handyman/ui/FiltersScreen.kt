package com.example.handyman.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.common_utils.ThreeValueSlider

// Use the standalone model in FiltersState.kt
import com.example.handyman.ui.FiltersState
import com.project.common_utils.components.BackArrowIcon
import com.project.common_utils.components.OrangeButton
import com.project.common_utils.components.ReviewStars

@Composable
fun FiltersScreen(
    onBack: () -> Unit,
    onApply: (FiltersState) -> Unit
) {
    var price by remember { mutableStateOf(15f) }        // $/h
    var rating by remember { mutableStateOf(4.0f) }      // stars
    var distance by remember { mutableStateOf(500f) }    // meters
    var plumber by remember { mutableStateOf(true) }
    var electrician by remember { mutableStateOf(false) }
    var carpenter by remember { mutableStateOf(false) }
    var painter by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackArrowIcon(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text("Filters", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(16.dp))

        // Price/hour
        Text("Price per hour", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Slider(
            value = price,
            onValueChange = { price = it },
            valueRange = 5f..100f,
            steps = 18 // ≈ $5 increments
        )
        Text("$ ${price.toInt()}/h")

        Spacer(Modifier.height(16.dp))

        // Minimum rating
        Text("Minimum rating (${String.format("%.1f", rating)})", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        ReviewStars(rating = rating, onRatingChange = { rating = it }, size = 28.dp)

        Spacer(Modifier.height(16.dp))

        // Distance (3-point slider)
        Text("Distance", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        ThreeValueSlider(
            valueRange = 0f..2000f, // meters
            startValue = 250f,
            midValue = 1000f,
            endValue = 2000f,
            onValueChange = { distance = it },
            modifier = Modifier.fillMaxWidth()
        )
        Text("${distance.toInt()} m")

        Spacer(Modifier.height(16.dp))

        // Categories (simple toggles)
        Text("Categories", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        FilterToggle("Plumber", plumber) { plumber = it }
        FilterToggle("Electrician", electrician) { electrician = it }
        FilterToggle("Carpenter", carpenter) { carpenter = it }
        FilterToggle("Painter", painter) { painter = it }

        Spacer(Modifier.weight(1f))

        OrangeButton(
            onClick = {
                onApply(
                    FiltersState(
                        pricePerHourMax = price.toInt(),
                        minRating = rating,
                        maxDistanceMeters = distance.toInt(),
                        categories = buildList {
                            if (plumber) add("Plumber")
                            if (electrician) add("Electrician")
                            if (carpenter) add("Carpenter")
                            if (painter) add("Painter")
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
