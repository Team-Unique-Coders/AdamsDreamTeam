package com.example.learn.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    var english by remember { mutableStateOf(true) }
    var math by remember { mutableStateOf(false) }
    var science by remember { mutableStateOf(false) }
    var history by remember { mutableStateOf(false) }
    var art by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Top bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackArrowIcon(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text("Filters", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(16.dp))

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

        Text(
            "Minimum rating (${String.format("%.1f", rating)})",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        ReviewStars(rating = rating, onRatingChange = { rating = it }, size = 28.dp)

        Spacer(Modifier.height(16.dp))

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

        Text("Categories", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        FilterToggle("English", english) { english = it }
        FilterToggle("Math", math) { math = it }
        FilterToggle("Science", science) { science = it }
        FilterToggle("History", history) { history = it }
        FilterToggle("Art", art) { art = it }

        Spacer(Modifier.weight(1f))

        OrangeButton(
            onClick = {
                onApply(
                    FiltersState(
                        pricePerHourMax = price.toInt(),
                        minRating = rating,
                        maxDistanceMeters = distance.toInt(),
                        categories = buildList {
                            if (english) add("English")
                            if (math) add("Math")
                            if (science) add("Science")
                            if (history) add("History")
                            if (art) add("Art")
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