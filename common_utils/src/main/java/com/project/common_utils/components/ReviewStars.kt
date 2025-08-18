package com.project.common_utils.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.round
import com.project.common_utils.R

@Composable
fun ReviewStars(
    rating: Float,
    onRatingChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    stars: Int = 5,
    allowHalf: Boolean = true,
    size: Dp = 24.dp,
    activeColor: Color = colorResource(R.color.orange),
    inactiveColor: Color = Color(0xFFD6D6D6),
    spacing: Dp = 8.dp
) {
    fun snap(v: Float): Float {
        val clamped = v.coerceIn(0f, stars.toFloat())
        return if (allowHalf) round(clamped * 2f) / 2f else round(clamped)
    }
    fun xToRating(x: Float, totalWidthPx: Float): Float {
        if (totalWidthPx <= 0f) return rating
        val p = (x / totalWidthPx).coerceIn(0f, 1f)
        return snap(p * stars)
    }

    var rowWidth by remember { mutableStateOf(0) }

    Row(
        modifier = modifier
            .onSizeChanged { rowWidth = it.width }
            .pointerInput(Unit) {
                // Tap to set rating
                detectTapGestures { offset ->
                    onRatingChange(xToRating(offset.x, rowWidth.toFloat()))
                }
            }
            .pointerInput(Unit) {
                // Drag to scrub rating
                detectDragGestures(onDrag = { change, _ ->
                    onRatingChange(xToRating(change.position.x, rowWidth.toFloat()))
                    change.consume()
                })
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(stars) { i ->
            val idx = i + 1
            // fraction of this star that should be filled: 0f, 0.5f, or 1f
            val thisStarFraction = when {
                rating >= idx -> 1f
                rating > idx - 1 -> if (allowHalf && rating >= idx - 0.5f) 0.5f else 0f
                else -> 0f
            }

            StarWithFraction(
                fraction = thisStarFraction,
                starSize = size,               // <-- renamed param
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                modifier = Modifier.padding(end = if (i != stars - 1) spacing else 0.dp)
            )
        }
    }
}

/** Draws an outlined star, then overlays a filled star clipped to [fraction] of width. */
@Composable
private fun StarWithFraction(
    fraction: Float, // 0f..1f
    starSize: Dp,    // renamed to avoid collision with DrawScope.size
    activeColor: Color,
    inactiveColor: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(starSize), contentAlignment = Alignment.CenterStart) {
        // base (empty) star
        Icon(
            imageVector = Icons.Outlined.Star,
            contentDescription = null,
            tint = inactiveColor,
            modifier = Modifier.matchParentSize()
        )
        if (fraction > 0f) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = activeColor,
                modifier = Modifier
                    .matchParentSize()
                    .drawWithContent {
                        // 'size' here is DrawScope.size (pixels), not the Dp param
                        val w = size.width * fraction.coerceIn(0f, 1f)
                        clipRect(left = 0f, top = 0f, right = w, bottom = size.height) {
                            this@drawWithContent.drawContent()
                        }
                    }
            )
        }
    }
}

/* --------- Preview like your OrangeButton preview --------- */

@Preview(showBackground = true)
@Composable
fun ReviewStarsPreview() {
    var rating by remember { mutableStateOf(3.5f) }
    Column(Modifier.padding(16.dp)) {
        ReviewStars(
            rating = rating,
            onRatingChange = { rating = it }, // tap/drag to change
            size = 28.dp
        )
    }
}
