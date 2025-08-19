package com.example.uber.ui

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.uber.R
import org.osmdroid.util.GeoPoint


@Composable
fun DriverOptionsBottomSheet(
    onDriverSelected: (Driver) -> Unit
) {
    val drivers = listOf(
        Driver("Gabriel F.", "Peugeot 308", "Red", 4.8, "3 min", "$8-10", R.drawable.adamplaceholder),
        Driver("Maria L.", "Toyota Prius", "White", 4.9, "5 min", "$9-11", R.drawable.adamplaceholder)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 50.dp, height = 4.dp)
                .background(Color.LightGray, RoundedCornerShape(50))
                .align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            "Available drivers",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        drivers.forEach { driver ->
            DriverOptionCard(driver) { onDriverSelected(driver) }
            Spacer(Modifier.height(12.dp))
        }
    }
}



data class Driver(
    val name: String,
    val car: String,
    val color: String,
    val rating: Double,
    val eta: String,
    val estimate: String,
    @DrawableRes val photo: Int
)

@Composable
fun DriverOptionCard(driver: Driver, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = driver.photo),
            contentDescription = driver.name,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(driver.name, fontWeight = FontWeight.Bold)
            Text("${driver.car} • ${driver.color}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFF9800), modifier = Modifier.size(16.dp))
                Text("${driver.rating}", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp))
                Text(driver.eta, style = MaterialTheme.typography.bodySmall)
            }
        }
        Text(driver.estimate, fontWeight = FontWeight.Bold)
    }
}
