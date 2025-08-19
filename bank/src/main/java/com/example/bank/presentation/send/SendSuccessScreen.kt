package com.example.bank.presentation.send

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
import com.project.common_utils.components.OrangeButton

@Composable
fun SendSuccessScreen(
    onGoHome: () -> Unit,
    // ⬇️ use your drawable here; if the resource name is different, just change this line
    @DrawableRes illustrationRes: Int = com.example.bank.R.drawable.sendconfirm
) {
    Scaffold(containerColor = Color.White) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            // Illustration
            if (illustrationRes != 0) {
                Image(
                    painter = painterResource(illustrationRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(top = 8.dp, bottom = 8.dp)
                )
            } else {
                // fallback (should never hit if your drawable exists)
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFFFF7A1A),
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Money sent",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFF0E1325)
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Your recipient will receive your payment.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.weight(1f))

            OrangeButton(
                onClick = onGoHome,
                text = "Go home"
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}
