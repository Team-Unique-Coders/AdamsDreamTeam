package com.example.laundry.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.laundry.components.MapsCompo
import com.example.laundry.data.Provider
import com.example.laundry.data.fake
import com.example.laundry.navigation.LaundryDestinations
import com.project.common_utils.components.OrangeButton
import dalvik.system.InMemoryDexClassLoader

@Composable
fun ProviderDetailScreen(
    provider: Provider = fake.first(),
    onBack: () -> Unit = {},
    onOpen: (String) -> Unit = {},
) {
    Scaffold(
        bottomBar = {
            Box(Modifier.fillMaxWidth().padding(16.dp)) {
                OrangeButton(onClick = {
                    onOpen(LaundryDestinations.SCHEDULESCREEN)
                }, "Take Appointment")
            }
        },
    ) { inner ->
        Column(Modifier.fillMaxSize().padding(inner)) {
            Box {
                // Header map
                MapsCompo(
                    latitude = provider.lat,
                    longitude = provider.lon,
                    zoom = 16.0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )

                // Back
                SmallFloatingActionButton(
                    onClick = onBack,
                    containerColor = Color.White,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFFFF8800))
                }

                // Overlapping avatar
                AsyncImage(
                    model = provider.photoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(112.dp)
                        .clip(CircleShape)
                        .align(Alignment.BottomCenter)
                        .offset(y = 56.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(64.dp))

            Column(Modifier.padding(horizontal = 20.dp)) {
                Text(provider.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("⭐ ${provider.rating}", style = MaterialTheme.typography.titleMedium)
                    Text(provider.priceText, style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.height(12.dp))
                Text(provider.about, color = Color.Gray)
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Column(Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
                Text(provider.address, style = MaterialTheme.typography.titleMedium)
            }
            HorizontalDivider()
            provider.services.forEach { (name, price) ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(name, style = MaterialTheme.typography.titleMedium)
                    Text(price, style = MaterialTheme.typography.titleMedium)
                }
                HorizontalDivider(thickness = 0.7.dp, color = Color(0x11000000))
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun DetailPreview() {
    MaterialTheme { ProviderDetailScreen(fake.first()) }
}
