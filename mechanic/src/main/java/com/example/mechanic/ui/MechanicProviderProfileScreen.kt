package com.example.mechanic.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.common_utils.*

@Composable
fun MechanicProviderProfileScreen(
    onBack: () -> Unit,
    onTakeAppointment: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackArrowIcon(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text("Jenny Jones", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.weight(1f))
            StarsIcon(onClick = {}, size = 24.dp) // decorative
        }

        Spacer(Modifier.height(16.dp))

        // Simple info card (match mock structure loosely)
        Card {
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                Text("4.8   •   $15/h", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text("Reliable car & motorcycle mechanic. 8+ years experience.")
                Spacer(Modifier.height(12.dp))
                Text("📍 28 Broad Street, Johannesburg")
                Spacer(Modifier.height(8.dp))
                Text("🛠️  Car, Motorcycle")
            }
        }

        Spacer(Modifier.weight(1f))
        OrangeButton(onClick = onTakeAppointment, text = "Take appointment")
    }
}
