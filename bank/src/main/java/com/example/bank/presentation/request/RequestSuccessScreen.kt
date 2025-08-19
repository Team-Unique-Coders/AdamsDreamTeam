package com.example.bank.presentation.request

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.common_utils.components.OrangeButton
import com.example.bank.R

@Composable
fun RequestSuccessScreen(
    onGoHome: () -> Unit,
    @DrawableRes illustration: Int = R.drawable.requestsent // reuse your asset
) {
    val brandOrange = Color(0xFFFF7A1A)

    Scaffold(containerColor = Color.White) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            // Illustration
            Image(
                painter = painterResource(illustration),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )

            Spacer(Modifier.height(24.dp))

            // Title
            Text(
                text = "Request sent",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0E1325),
                textAlign = Alignment.CenterHorizontally.let { TextAlign.Center }
            )

            Spacer(Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Your contact will receive your request.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.weight(1f))

            // Go home button
            OrangeButton(
                onClick = onGoHome,
                text = "Go home",
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}
