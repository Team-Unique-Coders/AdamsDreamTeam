package com.project.common_utils.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.utsman.osmandcompose.CameraState
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import org.osmdroid.util.GeoPoint

@Composable
fun MapComponent(
    latitude: Double = -6.3970066, longitude: Double = 106.8224316, zoom: Double = 12.0,
    modifier: Modifier = Modifier.fillMaxSize(),
    content: @Composable (CameraState.() -> Unit)? = null
) {
    // define camera state
    val cameraState = rememberCameraState {
        geoPoint = GeoPoint(latitude, longitude)
        this.zoom = zoom // optional, default is 5.0
    }

    // add node
    OpenStreetMap(
        modifier = modifier,
        cameraState = cameraState
    ){
        content?.invoke(cameraState)
    }
}

@Preview(showBackground = true)
@Composable
fun MapPreview() {
    MapComponent()
}