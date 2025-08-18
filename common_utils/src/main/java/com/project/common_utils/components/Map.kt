package com.project.common_utils.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.utsman.osmandcompose.CameraState
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import org.osmdroid.util.GeoPoint

@Composable
fun MapComponent(
    latitude: Double = -6.3970066,
    longitude: Double = 106.8224316,
    zoom: Double = 12.0,
    modifier: Modifier = Modifier.fillMaxSize(),
    isShowButton: Boolean = false,
    content: @Composable (CameraState.() -> Unit)? = null,

) {
    val cameraState = rememberCameraState {
        geoPoint = GeoPoint(latitude, longitude)
        this.zoom = zoom
    }

    Box(modifier) {
        OpenStreetMap(
            modifier = Modifier.matchParentSize(),
            cameraState = cameraState
        ) {
            content?.invoke(cameraState)
        }

        // Zoom buttons overlay (right side)
        if (isShowButton) Column(
            Modifier
                .align(Alignment.CenterEnd)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FloatingActionButton(
                onClick = { cameraState.zoom += 1.0 },
                containerColor = Color.White,
                modifier = Modifier.size(40.dp)
            ) {
                Text("+", style = MaterialTheme.typography.titleLarge, color = Color.Black)
            }
            FloatingActionButton(
                onClick = { cameraState.zoom -= 1.0 },
                containerColor = Color.White,
                modifier = Modifier.size(40.dp)
            ) {
                Text("-", style = MaterialTheme.typography.titleLarge, color = Color.Black)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MapPreview() {
    MapComponent()
}