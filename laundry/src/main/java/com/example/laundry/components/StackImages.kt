package com.example.laundry.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.laundry.R

@Composable
fun StackedImages() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f), // Keep it square, adjust as needed
        contentAlignment = Alignment.Center
    ) {
        // Background (larger) image
        Image(
            painter = painterResource(id = R.drawable.vector),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.6f) // Make it 80% of screen width
                .aspectRatio(1f), // Maintain original ratio
            contentScale = ContentScale.Fit
        )

        // Foreground (smaller) image on top
        Image(
            painter = painterResource(id = R.drawable.group_2),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.5f) // Smaller size
                .aspectRatio(1f), // Maintain aspect ratio
            contentScale = ContentScale.Fit
        )
        Image(
            painter = painterResource(id = R.drawable.ellipse),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.25f) // Smaller size
                .aspectRatio(1f)
                .offset(x = (-70).dp, y = (-60).dp), // Maintain aspect ratio

            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.ellipse_1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.12f) // Smaller size
                .aspectRatio(1f)
                .offset(x = (-83).dp, y = (-125).dp),// Maintain aspect ratio
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.ellipse_2),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.08f) // Smaller size
                .aspectRatio(1f)// Maintain aspect ratio
                .offset(x = (-80).dp, y = (-60).dp),
            contentScale = ContentScale.Crop
        )


    }
}
