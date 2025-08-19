package com.example.handyman.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.common_utils.components.MapComponent
import com.example.handyman.R as HmR

@Composable
fun MapScreen(
    onBack: () -> Unit,
    onOpenList: () -> Unit,
    onOpenProvider: () -> Unit   // <— NEW
) {
    Box(Modifier.fillMaxSize()) {
        MapComponent(latitude = -26.2041, longitude = 28.0473)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundChip(iconRes = HmR.drawable.return_home, onClick = onBack, modifier = Modifier.size(40.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                RoundChip(iconRes = HmR.drawable.options, onClick = { /* filters if you want */ }, modifier = Modifier.size(40.dp))
                RoundChipVector(vector = Icons.AutoMirrored.Filled.List, onClick = onOpenList, modifier = Modifier.size(40.dp))
            }
        }

        SearchBarOverlay(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 68.dp, start = 16.dp, end = 16.dp)
        )

        RoundChip(
            iconRes = HmR.drawable.location,
            onClick = { /* center map */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .size(56.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ProviderMiniCard(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenProvider() },   // <-- open profile
                photo = HmR.drawable.handyman_list,
                name = "Jenny Jones",
                rating = "4.8",
                distance = "4.5 Mile\nNearby"
            )
            ProviderMiniCard(
                modifier = Modifier.weight(1f),
                photo = HmR.drawable.handyman_list,
                name = "Jean Down",
                rating = "4.8",
                distance = "4.5 Mile\nNearby"
            )
        }
    }
}

/* ------ the helper composables from your previous version (unchanged) ------ */

@Composable
private fun RoundChip(
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        color = Color.White,
        shape = CircleShape,
        shadowElevation = 8.dp,
        modifier = modifier
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(painter = painterResource(iconRes), contentDescription = null, modifier = Modifier.size(22.dp))
        }
    }
}

@Composable
private fun RoundChipVector(
    vector: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        color = Color.White,
        shape = CircleShape,
        shadowElevation = 8.dp,
        modifier = modifier
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(vector, null, tint = Color(0xFFFF7A00))
        }
    }
}

@Composable
private fun SearchBarOverlay(modifier: Modifier = Modifier) {
    val text = remember { mutableStateOf("") }
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 10.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            value = text.value,
            onValueChange = { text.value = it },
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Filled.Search, null, tint = Color(0xFFB0B5BA)) },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun ProviderMiniCard(
    modifier: Modifier = Modifier,
    @DrawableRes photo: Int,
    name: String,
    rating: String,
    distance: String
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        tonalElevation = 0.dp,
        shadowElevation = 10.dp,
        modifier = modifier.height(120.dp)
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(photo),
                    contentDescription = name,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(10.dp))
                Text(name, fontWeight = FontWeight.SemiBold)
            }
            HorizontalDivider(color = DividerDefaults.color.copy(alpha = 0.4f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(HmR.drawable.onestar),
                        contentDescription = "rating",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(rating)
                }
                Text(distance, color = Color(0xFF90959B))
            }
        }
    }
}
