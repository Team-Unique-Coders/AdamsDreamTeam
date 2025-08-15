package com.project.common_utils


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun CircularImageHolderUrl(
    imageUrl: String,
    description: String,
    size: Dp = 100.dp,
    borderColor: Color = Color.Gray,
    borderWidth: Dp = 2.dp,
    elevation: Dp = 4.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .shadow(elevation, CircleShape)
            .clip(CircleShape)
            .border(borderWidth, borderColor, CircleShape)
            .background(Color.LightGray)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = description,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // centerCrop
        )
    }
}


@Composable
fun CircularImageHolderDrawable(
    drawableResId: Int,
    description: String,
    size: Dp = 100.dp,
    borderColor: Color = Color.Gray,
    borderWidth: Dp = 2.dp,
    elevation: Dp = 4.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .shadow(elevation, CircleShape)
            .clip(CircleShape)
            .border(borderWidth, borderColor, CircleShape)
            .background(Color.LightGray)
    ) {
        Image(
            painter = painterResource(id = drawableResId),
            contentDescription = description,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // centerCrop
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CircularImageHolderPreview() {
//        CircularImageHolder(imageUrl = "", description = "What you want to keep")
        CircularImageHolderDrawable(R.drawable.profiled,"helloword")
}
