package com.project.adamdreamteam.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.adamdreamteam.R
import com.project.adamdreamteam.navigation.Routes
import com.project.adamdreamteam.ui.theme.LocalBrand

data class FeatureItem(
    val title: String,
    val iconRes: Int,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(onOpen: (String) -> Unit) {
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }

    val featureItems: List<FeatureItem> = listOf(
        FeatureItem("Uber",     R.drawable.uber_icon,     Routes.UBER),
        FeatureItem("Tinder",   R.drawable.tinder_icon,   Routes.TINDER),
        FeatureItem("Delivery", R.drawable.delivery_icon, Routes.DELIVERY),
        FeatureItem("Learn",    R.drawable.learn_icon,    Routes.LEARN),
        FeatureItem("Chat",     R.drawable.chat_icon,     Routes.CHAT),
        FeatureItem("Doctor",   R.drawable.doctor_icon,   Routes.DOCTOR),
        FeatureItem("Laundry",  R.drawable.laundry_icon,  Routes.LAUNDRY),
        FeatureItem("Eat",      R.drawable.eat_icon,      Routes.EAT),
        FeatureItem("Hotel",    R.drawable.hotel_icon,    Routes.HOTEL),
        FeatureItem("Handyman", R.drawable.handyman_icon, Routes.HANDYMAN),
        FeatureItem("Mechanic", R.drawable.mechanic_icon, Routes.MECHANIC),
        FeatureItem("Bank",     R.drawable.bank_icon,     Routes.BANK)
    )
    val featureByRoute = remember(featureItems) { featureItems.associateBy { it.route } }


    var favoriteRoutes by rememberSaveable { mutableStateOf(listOf<String>()) }
    val favorites: List<FeatureItem> = favoriteRoutes.mapNotNull { featureByRoute[it] }

    fun toggleFavorite(route: String) {
        favoriteRoutes =
            if (favoriteRoutes.contains(route)) favoriteRoutes - route
            else favoriteRoutes + route
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("I Click I Pay") },
                actions = { TextButton(onClick = { showLogoutDialog = true }) { Text("Logout") } }
            )
        }
    ) { inner ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3), // 🔒 exactly 3 columns
            modifier = Modifier
                .padding(inner)
                .fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (favorites.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(Modifier.fillMaxWidth()) {
                        Text(
                            "Favorite Apps",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.height(12.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(favorites, key = { it.route }) { fav ->
                                FavoriteMiniTile(
                                    item = fav,
                                    onOpen = { onOpen(fav.route) },
                                    onUnfavorite = { toggleFavorite(fav.route) }
                                )
                            }
                        }
                    }
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "All Services",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            // ---------- All services grid (3 columns) ----------
            items(featureItems, key = { it.route }) { feature ->
                val isFav = favoriteRoutes.contains(feature.route)
                FeatureTile(
                    title = feature.title,
                    iconRes = feature.iconRes,
                    isFavorite = isFav,
                    onToggleFavorite = { toggleFavorite(feature.route) },
                    onClick = { onOpen(feature.route) }
                )
            }
        }
    }


    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log out?") },
            text = { Text("You can sign back in anytime.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        // TODO: FirebaseAuth.getInstance().signOut() later
                    }
                ) { Text("Log out") }
            },
            dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun FavoriteMiniTile(
    item: FeatureItem,
    onOpen: () -> Unit,
    onUnfavorite: () -> Unit
) {
    ElevatedCard(
        onClick = onOpen,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .width(110.dp)
                .height(120.dp)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(LocalBrand.current.gradient),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(8.dp)
        ) {
            Column(Modifier.fillMaxSize()) {

                // Top row: small heart, no overlap with icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 28.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Top
                ) {
                    IconButton(
                        onClick = onUnfavorite,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Remove from favorites",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                // Center content (smaller plate)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(item.iconRes),
                                contentDescription = item.title,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = item.title,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp),
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }
            }
        }
    }
}


@Composable
private fun FeatureTile(
    title: String,
    iconRes: Int,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(148.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        // Gradient border around the whole card
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(LocalBrand.current.gradient),
                    shape = MaterialTheme.shapes.large
                )
                .padding(12.dp) // inner padding
        ) {
            Column(Modifier.fillMaxSize()) {

                // Top row reserved for the heart (prevents overlap)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 36.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Top
                ) {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(36.dp) // compact, but still tappable
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite)
                                MaterialTheme.colorScheme.secondary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Centered content below the heart row
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Circular icon plate
                    Card(
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(76.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(iconRes),
                                contentDescription = title,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp),
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

