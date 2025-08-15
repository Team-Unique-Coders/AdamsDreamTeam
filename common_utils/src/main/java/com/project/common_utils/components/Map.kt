package com.project.common_utils.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import org.osmdroid.util.GeoPoint

@Composable
fun MapComponent(latitude: Double = -6.3970066, longitude: Double = 106.8224316){
    // define camera state
    val cameraState = rememberCameraState {
        geoPoint = GeoPoint(latitude, longitude)
        zoom = 12.0 // optional, default is 5.0
    }

    // add node
    OpenStreetMap(
        modifier = Modifier.fillMaxSize(),
        cameraState = cameraState
    )
}

@Preview(showBackground = true)
@Composable
fun MapPreview() {
    MapComponent()
}