package com.example.handyman.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import com.project.common_utils.components.BackArrowIcon
import com.project.common_utils.components.MapComponent
import com.project.common_utils.components.OrangeButton
import com.project.common_utils.components.ReviewStars

/**
 * Simple profile page for a handyman.
 * For now it uses dummy content. Later you can pass a real ProviderUi or id.
 */
@Composable
fun ProviderProfileScreen(
    onBack: () -> Unit,
    onTakeAppointment: () -> Unit
) {
    // Fake provider (matches mock: "Jenny Jones")
    val name = remember { "Jenny Jones" }
    var rating by remember { mutableStateOf(4.8f) }
    val pricePerHour = 15
    val address = "28 Broad Street\nJohannesburg"
    val roles = "Plumber\nCarpenter"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            BackArrowIcon(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text(text = name, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.weight(1f))
            Text(text = "$ $pricePerHour/h", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(Modifier.height(16.dp))

        // Avatar + rating
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // If you later have an actual drawable, swap in CircularImageHolderDrawable(drawableResId = R.drawable.xyz, ...)
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = name,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleMedium)
                ReviewStars(rating = rating, onRatingChange = { rating = it }, size = 22.dp)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Short bio
        Text(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis lobortis sit amet odio in egestas.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(16.dp))

        // Address card
        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
                Text("Address", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
                Spacer(Modifier.height(6.dp))
                Text(address, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Roles card
        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
                Text("Skills", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
                Spacer(Modifier.height(6.dp))
                Text(roles, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Map (from your common_utils)
        ElevatedCard(Modifier.fillMaxWidth().height(220.dp)) {
            MapComponent(latitude = -26.2041, longitude = 28.0473) // Johannesburg
        }

        Spacer(Modifier.height(20.dp))

        // CTA
        OrangeButton(onClick = onTakeAppointment, text = "Take appointment")
    }
}
