package com.example.learn.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learn.R
import com.project.common_utils.components.BackArrowIcon
import com.project.common_utils.components.OrangeButton

@Composable
fun LearnWelcomeScreen(
    onLetsGo: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackArrowIcon(onClick = onBack)
        }
        Spacer(Modifier.height(32.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.welcome_img), // Replace 'my_image' with your image file name
                contentDescription = "A description of the image for accessibility", // Important for accessibility
                modifier = Modifier // Optional: Add modifiers for size, alignment, etc.
                // colorFilter = ColorFilter.tint(Color.Red) // Optional: Apply a tint
            )
            Spacer(Modifier.height(36.dp))
            Text(
                text = "Tutor",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Get matched with your ideal tutor for all of your educational needs",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.weight(1f))
        OrangeButton(onClick = onLetsGo, text = "Let's go")

    }
}

@Preview
@Composable
private fun LearnWelcomePreview() {
    LearnWelcomeScreen(onLetsGo = {}, onBack = {})
}