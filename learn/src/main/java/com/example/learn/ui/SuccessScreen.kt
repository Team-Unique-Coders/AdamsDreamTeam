package com.example.learn.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.common_utils.OrangeButton

@Composable
fun LearnSuccessScreen(
    onGoHome: () -> Unit,
    onViewOrders: () -> Unit = onGoHome // placeholder; hook to orders later
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = "Success",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(96.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Order placed!",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "We’ve sent your booking details to the provider.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(32.dp))
        OrangeButton(onClick = onGoHome, text = "Back to Home")
        Spacer(Modifier.height(12.dp))
        // If you later add an Orders screen, hook this up:
        // OrangeButton(onClick = onViewOrders, text = "View orders")
    }
}