package com.example.uber.ui

import android.widget.RatingBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.draw.clip

import androidx.compose.runtime.*
import org.osmdroid.util.GeoPoint

@Composable
fun RideConfirmScreen(
    driver: Driver,
    pickupLabel: String,
    pickupAddress: String,
    destinationLabel: String,
    destinationAddress: String,
    onCancel: () -> Unit,
    onOrder: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Your car", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                "Cancel Ride",
                color = Color.Red,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onCancel() }
            )
        }
        Spacer(Modifier.height(16.dp))
        DriverInfoCard(driver = driver, estimate = driver.estimate)
        Spacer(Modifier.height(24.dp))
        LocationConfirmItem("Destination location", destinationLabel, destinationAddress, Color(0xFF00C853))
        LocationConfirmItem("Pick up location", pickupLabel, pickupAddress, Color(0xFF2979FF))
        Spacer(Modifier.weight(1f))
        Button(
            onClick = { onOrder() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6D00)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Order", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DriverOnTheWayScreen(
    driver: Driver,
    estimate: String,
    time: String,
    onBackToMap: () -> Unit,
    onTripStarted: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            //
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text("Your car", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Your driver is coming in $time", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        DriverInfoCard(driver = driver, estimate = estimate)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = { onBackToMap() }) {
                Text("Back to map")
            }
            Button(onClick = { onTripStarted() }) {
                Text("Start trip")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        LocationConfirmItem("Destination location", "Work", "28 Broad Street\nJohannesburg", Color(0xFF00C853))
        LocationConfirmItem("Pick up location", "Home", "28 Orchard Road\nJohannesburg", Color(0xFF2979FF))
    }
}



@Composable
fun OnTripScreen(
    driver: Driver,
    estimate: String,
    time: String,
    onBackToMap: () -> Unit,
    onTripEnded: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text(
            "You arrive in $time",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        DriverInfoCard(driver = driver, estimate = estimate)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = { onBackToMap() }) {
                Text("Back to Map")
            }
            Button(onClick = { onTripEnded() }) {
                Text("End Trip")
            }
        }
    }
}



@Composable
fun TripEndedScreen(
    onRatingSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text("You arrived!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Arrival time", fontWeight = FontWeight.Bold)
                Text("10:04")
            }
            Column {
                Text("Final cost", fontWeight = FontWeight.Bold)
                Text("$9.50")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Rate your trip with Gabriel F.", style = MaterialTheme.typography.bodyLarge)
        RatingBar(rating = 3, onRatingChanged = onRatingSelected)
    }
}

@Composable
fun PostRatingScreen(
    rating: Int,
    onTipSelected: (Int) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Thank you", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("You rate Gabriel $rating stars", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        RatingBar(rating = rating, onRatingChanged = {})
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Say something about Gabriel’s service") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Do you want to tip Gabriel?", style = MaterialTheme.typography.bodyLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf(2, 5, 10).forEach { tip ->
                Button(onClick = { onTipSelected(tip) }) {
                    Text("$$tip")
                }
            }
            OutlinedButton(onClick = { }) {
                Text("Other")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}


data class SavedPlace(
    val label: String,
    val address: String,
    val icon: Int,
    val color: Color,
    val coords: GeoPoint
)

@Composable
fun DriverInfoCard(driver: Driver, estimate: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF9F9F9))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = driver.photo),
            contentDescription = driver.name,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
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
        Text(estimate, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

@Composable
fun RatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    stars: Int = 5
) {
    Row(modifier = modifier) {
        for (i in 1..stars) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (i <= rating) Color(0xFFFF9800) else Color.Gray,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onRatingChanged(i) }
            )
        }
    }
}
@Composable
fun PaymentScreen(
    balance: Double,
    accountName: String,
    accountNumber: String,
    totalAmount: Double,
    onPay: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Balance
        Text("Your balance", style = MaterialTheme.typography.bodyLarge)
        Text(
            text = "$${String.format("%,.2f", balance)}",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFC107)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(accountName, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("ACCOUNT NUMBER", style = MaterialTheme.typography.labelMedium)
                Text(accountNumber, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Fees and total
        Text("Transaction fees", style = MaterialTheme.typography.bodyMedium)
        Text("$0", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(12.dp))

        Text("Total amount", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = "$${String.format("%,.2f", totalAmount)}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF5722)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Balance after payment
        Text("Your balance after payment", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = "$${String.format("%,.2f", balance - totalAmount)}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Pay button
        Button(
            onClick = onPay,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Pay", color = Color.White, style = MaterialTheme.typography.titleMedium)
        }
    }
}


@Composable
fun LocationConfirmItem(
    label: String,
    title: String,
    address: String,
    color: Color
) {
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = color)
            Spacer(Modifier.width(8.dp))
            Column {
                Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(address, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}