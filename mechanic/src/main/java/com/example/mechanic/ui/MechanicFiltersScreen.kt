package com.example.mechanic.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.common_utils.BackArrowIcon
import com.project.common_utils.OrangeButton
import com.project.common_utils.ReviewStars
import com.project.common_utils.ThreeValueSlider

@Composable
fun MechanicFiltersScreen(
    onBack: () -> Unit,
    onApply: () -> Unit
) {
    var price by remember { mutableStateOf(15f) }
    var rating by remember { mutableStateOf(4.0f) }
    var distance by remember { mutableStateOf(500f) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackArrowIcon(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text("Filters", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(16.dp))

        Text("Price per hour", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Slider(value = price, onValueChange = { price = it }, valueRange = 5f..100f, steps = 18)
        Text("$ ${price.toInt()}/h")

        Spacer(Modifier.height(16.dp))
        Text("Minimum rating (${String.format("%.1f", rating)})", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        ReviewStars(rating = rating, onRatingChange = { rating = it }, size = 28.dp)

        Spacer(Modifier.height(16.dp))
        Text("Distance", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        ThreeValueSlider(
            valueRange = 0f..2000f,
            startValue = 250f, midValue = 1000f, endValue = 2000f,
            onValueChange = { distance = it },
            modifier = Modifier.fillMaxWidth()
        )
        Text("${distance.toInt()} m")

        Spacer(Modifier.weight(1f))
        OrangeButton(onClick = onApply, text = "Apply")
    }
}
