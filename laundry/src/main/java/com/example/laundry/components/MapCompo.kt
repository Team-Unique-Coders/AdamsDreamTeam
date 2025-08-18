package com.example.laundry.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.utsman.osmandcompose.CameraState
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import org.osmdroid.util.GeoPoint

@Composable
fun MapsCompo(
    latitude: Double = -6.3970066,
    longitude: Double = 106.8224316,
    zoom: Double = 12.0,
    modifier: Modifier = Modifier.fillMaxSize(),
    content: @Composable (CameraState.() -> Unit)? = null
) {
    val cameraState = rememberCameraState {
        geoPoint = GeoPoint(latitude, longitude)
        this.zoom = zoom
    }
    OpenStreetMap(
        modifier = modifier,
        cameraState = cameraState
    ) {
        content?.invoke(cameraState)
    }
}