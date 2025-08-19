package com.example.uber

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline


@Composable
fun OSMMap(
    latitude: Double = 33.7490,
    longitude: Double = -84.3880,
    focusPoint: GeoPoint? = null,
    destination: GeoPoint? = null
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            MapView(context).apply {
                setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
                controller.setZoom(15.0)
                controller.setCenter(GeoPoint(latitude, longitude))
            }
        },
        update = { mapView ->
            val startPoint = GeoPoint(latitude, longitude)

            if (focusPoint != null) {
                mapView.controller.setZoom(19.5)
                mapView.controller.animateTo(focusPoint)
            }

            if (destination != null) {
                val line = Polyline().apply {
                    addPoint(startPoint)
                    addPoint(destination)
                    outlinePaint.color = Color.BLUE
                    outlinePaint.strokeWidth = 8f
                }
                mapView.overlays.add(line)
                mapView.invalidate()
            }
        }
    )
}


