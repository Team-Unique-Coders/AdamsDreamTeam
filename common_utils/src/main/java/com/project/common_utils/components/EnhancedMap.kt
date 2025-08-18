package com.project.common_utils.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.common_utils.R
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.rememberMarkerState
import org.osmdroid.util.GeoPoint

@Composable
fun EnhancedMapComponent(
    providers: List<Provider> = fake,
    onOpen: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    val center = providers.firstOrNull()
        ?.let { GeoPoint(it.lat, it.lon) }
        ?: GeoPoint(-6.397, 106.822)

    var query by remember { mutableStateOf("") }

    Box(
        Modifier
            .fillMaxSize()
            .safeContentPadding()
    ) {
        MapComponent(center.latitude, center.longitude, zoom = 14.0) {
            providers.forEach { p ->
                Marker(
                        state = rememberMarkerState(geoPoint = GeoPoint(p.lat, p.lon)),
                    )
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            SmallFloatingActionButton(
                onClick = { onBack() },
                containerColor = Color.White
            ) {
                Icon(Icons.Outlined.Home, contentDescription = "Home", tint = Color(0xFFFF8800))
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

        ProviderRow(
            providers = providers,
            onOpen = onOpen,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun ProviderRow(
    providers: List<Provider>,
    modifier: Modifier = Modifier,
    onOpen: () -> Unit = {},
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
                    .clickable { onOpen() }
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    CircularImageHolderDrawable(
                        R.drawable.profiled,
                        "holder"
                    )
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

@Preview(showBackground = true)
@Composable
private fun EnhancedMapPreview() {
    MaterialTheme { EnhancedMapComponent() }
}