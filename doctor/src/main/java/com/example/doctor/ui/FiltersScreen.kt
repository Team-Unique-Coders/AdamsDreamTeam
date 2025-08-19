package com.example.doctor.ui

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
fun DoctorFiltersScreen(
    onBack: () -> Unit,
    onApply: (FiltersState) -> Unit
) {
    var price by remember { mutableStateOf(15f) }        // $/h
    var rating by remember { mutableStateOf(4.0f) }      // stars
    var distance by remember { mutableStateOf(500f) }
    var orthodontist by remember { mutableStateOf(true) }
    var cardiologist by remember { mutableStateOf(false) }
    var gynecologist by remember { mutableStateOf(false) }
    var neurologist by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        FilterToggle("Orthodontist", orthodontist) { orthodontist = it }
        FilterToggle("Cardiologist", cardiologist) { cardiologist = it }
        FilterToggle("Gynecologist", gynecologist) { gynecologist = it }
        FilterToggle("Neurologist", neurologist) { neurologist = it }

        Spacer(Modifier.weight(1f))

        OrangeButton(
            onClick = {
                onApply(
                    FiltersState(
                        pricePerHourMax = price.toInt(),
                        minRating = rating,
                        maxDistanceMeters = distance.toInt(),
                        categories = buildList {
                            if (orthodontist) add("Orthodontist")
                            if (cardiologist) add("Cardiologist")
                            if (gynecologist) add("Gynecologist")
                            if (neurologist) add("Neurologist")
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