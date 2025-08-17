package com.example.mechanic.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.common_utils.OrangeButton

@Composable
fun MechanicSuccessScreen(onGoHome: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Order placed!", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
        Spacer(Modifier.height(12.dp))
        Text("Your mechanic appointment is scheduled.", textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        OrangeButton(onClick = onGoHome, text = "Done")
    }
}
