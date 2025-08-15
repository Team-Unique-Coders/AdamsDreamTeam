package com.project.common_utils.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.project.common_utils.R

/**
 * Reusable icon button that renders a drawable (PNG/JPG/SVG) as an icon.
 *
 * - Pass a drawableRes from your module's `res/drawable` (e.g., R.drawable.stars).
 * - `tint = null` (default) keeps the *original colors* of the PNG.
 * - Set `tint = Color(...)` if you want to recolor monochrome assets.
 */
@Composable
fun ButtonIcon(
    drawableRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    tint: Color? = null,
    contentDescription: String? = null,
    background: Color = Color.Transparent, // set to, e.g., Color(0x1A000000) for a subtle circle bg
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(size + 16.dp) // a little touch target padding
            .background(background, CircleShape),
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = tint ?: Color.Unspecified
        )
    ) {
        Icon(
            painter = painterResource(drawableRes),
            contentDescription = contentDescription,
            tint = tint ?: Color.Unspecified,   // keep original PNG colors when null
            modifier = Modifier.size(size)
        )
    }
}

/* ---------- Convenience wrappers for your specific assets ---------- */

@Composable
fun BackArrowIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    tint: Color? = null,
    background: Color = Color.Transparent,
    contentDescription: String? = "Back"
) = ButtonIcon(
    drawableRes = R.drawable.backarrow,
    onClick = onClick,
    modifier = modifier,
    size = size,
    tint = tint,
    contentDescription = contentDescription,
    background = background
)

@Composable
fun HamburgerIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    tint: Color? = null,
    background: Color = Color.Transparent,
    contentDescription: String? = "Menu"
) = ButtonIcon(
    drawableRes = R.drawable.hamburger,
    onClick = onClick,
    modifier = modifier,
    size = size,
    tint = tint,
    contentDescription = contentDescription,
    background = background
)

@Composable
fun HeartIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    tint: Color? = null,
    background: Color = Color.Transparent,
    contentDescription: String? = "Favorite"
) = ButtonIcon(
    drawableRes = R.drawable.heart,
    onClick = onClick,
    modifier = modifier,
    size = size,
    tint = tint,
    contentDescription = contentDescription,
    background = background
)

@Composable
fun NavIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    tint: Color? = null,
    background: Color = Color.Transparent,
    contentDescription: String? = "Navigate"
) = ButtonIcon(
    drawableRes = R.drawable.nav,
    onClick = onClick,
    modifier = modifier,
    size = size,
    tint = tint,
    contentDescription = contentDescription,
    background = background
)

@Composable
fun OptionsIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    tint: Color? = null,
    background: Color = Color.Transparent,
    contentDescription: String? = "Options"
) = ButtonIcon(
    drawableRes = R.drawable.options,
    onClick = onClick,
    modifier = modifier,
    size = size,
    tint = tint,
    contentDescription = contentDescription,
    background = background
)

@Composable
fun SearchIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    tint: Color? = null,
    background: Color = Color.Transparent,
    contentDescription: String? = "Search"
) = ButtonIcon(
    drawableRes = R.drawable.search,
    onClick = onClick,
    modifier = modifier,
    size = size,
    tint = tint,
    contentDescription = contentDescription,
    background = background
)

@Composable
fun StarsIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    tint: Color? = null,
    background: Color = Color.Transparent,
    contentDescription: String? = "Stars"
) = ButtonIcon(
    drawableRes = R.drawable.stars,
    onClick = onClick,
    modifier = modifier,
    size = size,
    tint = tint,
    contentDescription = contentDescription,
    background = background
)

/* ---------- Action helper like your OrangeButton ---------- */
fun getIconClickAction(name: String = "icon"): () -> Unit = {
    println("Clicked: $name")
}

/* ---------- Preview (like your OrangeButtonPreview) ---------- */

@Preview(showBackground = true)
@Composable
fun ButtonIconPreview() {
    // Example: tinted heart on a subtle circular background
    HeartIcon(
        onClick = getIconClickAction("heart"),
        size = 28.dp,
        tint = null,                       // keep PNG’s original color; set a Color to tint
        background = Color(0x1A000000)     // 10% black circle
    )
}
