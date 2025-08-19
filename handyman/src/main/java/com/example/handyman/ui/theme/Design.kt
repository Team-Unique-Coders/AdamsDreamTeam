// ui/theme/Design.kt
package com.example.handyman.ui.theme

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

// Reusable radii
val CardRadius = 16.dp
val BigRadius = 20.dp
val SmallRadius = 12.dp

@Composable
fun OrangeHeaderCardColors() = CardDefaults.cardColors(
    containerColor = MaterialTheme.colorScheme.primary
)

@Composable
fun SurfaceCardColors() = CardDefaults.cardColors(
    containerColor = MaterialTheme.colorScheme.surface
)
