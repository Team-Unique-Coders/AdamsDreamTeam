package com.example.doctor.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.doctor.R
import com.project.common_utils.components.OrangeButton

@Composable
fun DoctorWelcomeScreen(onLetsGo: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .safeContentPadding(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))

        Image(
            painter = painterResource(id = R.drawable.doctor_img), // Replace 'my_image' with your image file name
            contentDescription = "A description of the image for accessibility", // Important for accessibility
            modifier = Modifier // Optional: Add modifiers for size, alignment, etc.
            // colorFilter = ColorFilter.tint(Color.Red) // Optional: Apply a tint
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Health", style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "Seek help from a qualified and experienced doctor",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        OrangeButton(onClick = onLetsGo, "Let's go")
    }
}