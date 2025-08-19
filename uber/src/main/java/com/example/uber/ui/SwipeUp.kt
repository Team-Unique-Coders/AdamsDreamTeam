package com.example.uber.ui

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.uber.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun RideFlowScreen() {
    var step by remember { mutableStateOf("destination") }
    var pickupLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var destinationLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var selectedDriver by remember { mutableStateOf<Driver?>(null) }
    var tripRating by remember { mutableStateOf(0) }
    var selectedTip by remember { mutableStateOf(0) }

    when (step) {
        "destination" -> {
            MapWithBottomContent(
                pickup = GeoPoint(33.7488, -84.3885),
                destination = GeoPoint(33.7488, -84.3885)
            ) {
                DestinationBottomSheet { dest ->
                    destinationLocation = dest
                    step = "pickup"
                }
            }
        }


        "pickup" -> {
            MapWithBottomContent(
                pickup = GeoPoint(33.7488, -84.3885),
                destination = destinationLocation ?: GeoPoint(33.7542, -84.3900)
            ) {
                PickupBottomSheet(
                    onPickupSelected = { pick ->
                        pickupLocation = pick
                        step = "map_driver_options"
                    }
                )
            }
        }

// in your when(step)
        "map_driver_options" -> {
            if (pickupLocation != null && destinationLocation != null) {
                MapWithBottomContent(
                    pickupLocation!!,
                    destinationLocation!!
                ) {
                    DriverOptionsBottomSheet { driver ->
                        selectedDriver = driver
                        step = "ride_confirm"
                    }
                }
            }
        }

        "ride_confirm" -> {
            if (pickupLocation != null && destinationLocation != null && selectedDriver != null) {
                RideConfirmScreen(
                    driver = selectedDriver!!,
                    pickupLabel = "Pickup",
                    pickupAddress = "123 Peachtree St, Atlanta, GA",
                    destinationLabel = "Destination",
                    destinationAddress = "456 Piedmont Ave, Atlanta, GA",
                    onCancel = {
                        step = "map_driver_options"
                    },
                    onOrder = {
                        step = "driver_on_way"
                    }
                )
            }
        }

        "driver_on_way" -> {
            if (pickupLocation != null && destinationLocation != null && selectedDriver != null) {
                MapWithBottomContent(
                    pickupLocation!!,
                    destinationLocation!!
                ) {
                    DriverOnTheWayScreen(
                        driver = selectedDriver!!,
                        estimate = selectedDriver!!.estimate,
                        time = "5 min",
                        onBackToMap = {
                            step = "map_driver_options"
                        },
                        onTripStarted = {
                            step = "on_trip"
                        }
                    )
                }
            }
        }


        "on_trip" -> {
            if (pickupLocation != null && destinationLocation != null && selectedDriver != null) {
                MapWithBottomContent(
                    pickupLocation!!,
                    destinationLocation!!
                ) {
                    OnTripScreen(
                        driver = selectedDriver!!,
                        estimate = selectedDriver!!.estimate,
                        time = "15 min",
                        onBackToMap = {
                            step = "map_driver_options"
                        },
                        onTripEnded = {
                            step = "trip_ended"
                        }
                    )
                }
            }
        }

        "trip_ended" -> {
            TripEndedScreen(
                onRatingSelected = { rating ->
                    tripRating = rating
                    step = "post_rating"
                }
            )
        }

        "post_rating" -> {
            PostRatingScreen(
                rating = tripRating,
                onTipSelected = { tip -> selectedTip = tip },
                onSubmit = {
                    step = "payment"
                }
            )
        }

        "payment" -> {
            PaymentScreen(
                balance = 5523.26,
                accountName = "John Doe",
                accountNumber = "8014 8014 8014 8014",
                totalAmount = 9.50 + (selectedTip ?: 0)
            ) {
                step = "destination"
            }
        }
    }
}



@Composable
fun MapWithBottomContent(
    pickup: GeoPoint,
    destination: GeoPoint,
    bottomContent: @Composable () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        OSMMap(
            pickup = pickup,
            destination = destination
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            bottomContent()
        }
    }
}



@Composable
fun PickupBottomSheet(
    onPickupSelected: (GeoPoint) -> Unit
) {
    var selectedPlace by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            //
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
            "Set pick up location",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        if (selectedPlace == null) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search pickup") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
            )
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SavedQuickPlace("Home", R.drawable.home, Color(0xFF2979FF)) {
                    selectedPlace = "Home"
                    onPickupSelected(GeoPoint(33.7488, -84.3885))
                }
                SavedQuickPlace("Work", R.drawable.work, Color(0xFF00C853)) {
                    selectedPlace = "Work"
                    onPickupSelected(GeoPoint(33.7542, -84.3900))
                }
                SavedQuickPlace("Gym", R.drawable.gym, Color(0xFFFF6D00)) {
                    selectedPlace = "Gym"
                    onPickupSelected(GeoPoint(33.7517, -84.3920))
                }
            }

            Spacer(Modifier.height(16.dp))
            SavedPlaceItem(
                icon = R.drawable.work,
                label = "Work",
                address = "123 Peachtree Street, Atlanta, GA",
                color = Color(0xFF00C853)
            ) {
                selectedPlace = "Work"
                onPickupSelected(GeoPoint(33.7542, -84.3900))
            }
            SavedPlaceItem(
                icon = R.drawable.gym,
                label = "Gym",
                address = "200 Piedmont Ave NE, Atlanta, GA",
                color = Color(0xFFFF6D00)
            ) {
                selectedPlace = "Gym"
                onPickupSelected(GeoPoint(33.7517, -84.3920))
            }
        } else {
            SavedPlaceItem(
                icon = R.drawable.home,
                label = selectedPlace!!,
                address = "28 Orchard Road, Atlanta, GA",
                color = Color(0xFF2979FF)
            ) {
                selectedPlace = null
            }
        }
    }
}

@Composable
fun DestinationBottomSheet(
    onDestinationSelected: (GeoPoint) -> Unit
) {
    var selectedPlace by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            //
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
            "Set destination location",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        if (selectedPlace == null) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search destination") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
            )
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SavedQuickPlace("Home", R.drawable.home, Color(0xFF2979FF)) {
                    selectedPlace = "Home"
                    onDestinationSelected(GeoPoint(33.7488, -84.3885))
                }
                SavedQuickPlace("Work", R.drawable.work, Color(0xFF00C853)) {
                    selectedPlace = "Work"
                    onDestinationSelected(GeoPoint(33.7542, -84.3900))
                }
                SavedQuickPlace("Gym", R.drawable.gym, Color(0xFFFF6D00)) {
                    selectedPlace = "Gym"
                    onDestinationSelected(GeoPoint(33.7517, -84.3920))
                }
            }

            Spacer(Modifier.height(16.dp))
            SavedPlaceItem(
                icon = R.drawable.work,
                label = "Work",
                address = "123 Peachtree Street, Atlanta, GA",
                color = Color(0xFF00C853)
            ) {
                selectedPlace = "Work"
                onDestinationSelected(GeoPoint(33.7542, -84.3900))
            }
            SavedPlaceItem(
                icon = R.drawable.gym,
                label = "Gym",
                address = "200 Piedmont Ave NE, Atlanta, GA",
                color = Color(0xFFFF6D00)
            ) {
                selectedPlace = "Gym"
                onDestinationSelected(GeoPoint(33.7517, -84.3920))
            }
        } else {
            SavedPlaceItem(
                icon = R.drawable.home,
                label = selectedPlace!!,
                address = "28 Orchard Road, Atlanta, GA",
                color = Color(0xFF2979FF)
            ) {
                selectedPlace = null
            }
        }
    }
}


@Composable
fun SavedQuickPlace(
    label: String,
    @DrawableRes icon: Int,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                tint = color
            )
        }
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}


@Composable
fun OSMMap(
    pickup: GeoPoint,
    destination: GeoPoint
) {
    val context = LocalContext.current

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            Log.d("OSMMap", "Factory: creating MapView")

            Configuration.getInstance().load(
                context,
                PreferenceManager.getDefaultSharedPreferences(context)
            )
            Configuration.getInstance().userAgentValue = context.packageName
            Log.d("OSMMap", "UserAgent set to ${Configuration.getInstance().userAgentValue}")

            val mapView = MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                controller.setZoom(18.0)
                controller.setCenter(pickup)

                addMarker(pickup, "Pickup")
                addMarker(destination, "Destination")
                drawStraightLine(pickup, destination)

                onResume()
            }

            Log.d("OSMMap", "MapView initialized. Center=${pickup.latitude},${pickup.longitude}")
            mapView
        },
        update = { mapView ->
            Log.d("OSMMap", "Update called: recentering to ${pickup.latitude},${pickup.longitude}")
            mapView.controller.setCenter(pickup)
        }
    )
}


fun MapView.addMarker(point: GeoPoint, title: String) {
    val marker = Marker(this).apply {
        position = point
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        this.title = title
    }
    overlays.add(marker)
}

fun MapView.drawStraightLine(start: GeoPoint, end: GeoPoint) {
    val line = Polyline().apply {
        setPoints(listOf(start, end))
        outlinePaint.color = android.graphics.Color.BLUE
        outlinePaint.strokeWidth = 8f
    }
    overlays.add(line)
    invalidate()
}

@Composable
fun SavedPlaceItem(
    @DrawableRes icon: Int,
    label: String,
    address: String,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color.copy(alpha = 0.15f), RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                tint = color
            )
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(address, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}
