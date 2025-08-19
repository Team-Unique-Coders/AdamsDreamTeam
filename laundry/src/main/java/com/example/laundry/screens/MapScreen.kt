package com.example.laundry.screens

import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.example.laundry.data.Provider
import com.example.laundry.data.fake
import com.example.laundry.data.providerKey
import com.example.laundry.navigation.LaundryDestinations
import com.project.common_utils.R
import com.project.common_utils.components.CircularImageHolderDrawable
import com.project.common_utils.components.CircularImageHolderUrl
import com.project.common_utils.components.MapComponent
import com.utsman.osmandcompose.CameraState
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import org.osmdroid.util.GeoPoint

@Composable
fun LaundryMapScreen(
    providers: List<Provider> = fake,
    onProviderClick: (Provider) -> Unit = {},
    onOpen: (String) -> Unit = {},
    onBack: () -> Unit = {},
) {
    val center = providers.firstOrNull()
        ?.let { GeoPoint(it.lat, it.lon) }
        ?: GeoPoint(-6.397, 106.822)

    var query by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize()) {
        // Map with provider markers
        MapComponent(
            latitude = center.latitude,
            longitude = center.longitude,
            zoom = 14.0,
            isShowButton = true
        ) {
            providers.forEach { p ->
                    val state = rememberMarkerState(geoPoint = GeoPoint(p.lat, p.lon))
                Marker(
                    state = state,
                    onClick = { onProviderClick(p); true },
                ) {
                    AsyncImage(
                        model = p.photoUrl,
                        contentDescription = p.name,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable {
                                onOpen(LaundryDestinations.details(providerKey(p)))
                            }
                            .border(2.dp, Color.White, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // Top search & filter
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                SmallFloatingActionButton(
                    onClick = onBack,
                    containerColor = Color.White,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFFFF8800))
                }
                SmallFloatingActionButton(
                    onClick = { /* open filters */ },
                    containerColor = Color.White
                ) {
                    Icon(Icons.Outlined.Menu, contentDescription = "Filter", tint = Color(0xFFFF8800))
                }
            }
            Spacer(Modifier.height(8.dp))
            TextField(
                value = query,
                onValueChange = { query = it },
                singleLine = true,
                placeholder = { Text("Search") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

        }



        // Locate me
        FloatingActionButton(
            onClick = { /* TODO: animate camera to user's location */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color.White
        ) {
            Icon(Icons.Filled.LocationOn, contentDescription = "My location", tint = Color(0xFFFF8800))
        }

        // Bottom provider cards
        ProviderRow(
            providers = providers,
            onOpen=onOpen,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            onProviderClick = onProviderClick
        )
    }
}

@Composable
private fun ProviderRow(
    providers: List<Provider>,
    modifier: Modifier = Modifier,
    onProviderClick: (Provider) -> Unit,
    onOpen: (String) -> Unit = {},
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(providers) { p ->
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .width(300.dp)
                    .clickable { onOpen(LaundryDestinations.details(providerKey(p)))}
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    CircularImageHolderUrl(p.photoUrl,"url")
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(p.name, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(6.dp))
                        Row {
                            Text("⭐ ${p.rating}")
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text("${p.distanceMiles} Mile", color = Color.Gray)
                                Text("Nearby", color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

/* ───────────────────────── Previews ───────────────────────── */

@Preview(showBackground = true)
@Composable
private fun MapPreview() {
    MaterialTheme { LaundryMapScreen() }
}
