package com.example.handyman.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.handyman.R as HmR
import com.example.handyman.ui.theme.HandymanTheme

/* ---------------- theme wrapper ---------------- */
@Composable
private fun HmScreen(content: @Composable () -> Unit) {
    HandymanTheme { content() }
}

/* ---------------- PUBLIC SCREEN ---------------- */
@Composable
fun ProviderListScreen(
    onBack: () -> Unit,
    onOpenProvider: (ProviderUi) -> Unit,
    onOpenFilters: () -> Unit,
    onOpenMap: () -> Unit,                 // -> MapScreen
    filters: FiltersState? = null,
    @DrawableRes bannerResId: Int? = null
) = HmScreen {

    /* Sample data */
    val all = remember {
        listOf(
            ProviderUi("1", "Jessy Jones", "Plumber • Carpenter", 15, 4.8f, 500),
            ProviderUi("2", "Jenny Jones", "Electrician",         22, 4.6f, 900),
            ProviderUi("3", "Jean Down",   "Painter",              12, 4.2f, 450),
            ProviderUi("4", "John Craft",  "Carpenter",            30, 4.9f, 1500),
        )
    }

    /* Favourites + filtering */
    val favorites = remember { mutableStateListOf<String>() }
    var showOnlyFavorites by remember { mutableStateOf(false) }

    val filtered = remember(all, filters, favorites, showOnlyFavorites) {
        all.filter { p ->
            val favOk = if (showOnlyFavorites) favorites.contains(p.id) else true
            val otherOk = filters?.let {
                val priceOk = p.pricePerHour <= it.pricePerHourMax
                val ratingOk = p.rating >= it.minRating
                val distanceOk = p.distanceMeters <= it.maxDistanceMeters
                val catOk = it.categories.isEmpty() ||
                        it.categories.any { c -> p.role.contains(c, ignoreCase = true) }
                priceOk && ratingOk && distanceOk && catOk
            } ?: true
            favOk && otherOk
        }
    }

    val headerImageRes = bannerResId?.takeIf { it != 0 } ?: HmR.drawable.handyman_list

    Scaffold(containerColor = Color(0xFFF4F5F7)) { inner ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            /* ===== Banner + floating white card ===== */
            item {
                Box(Modifier.fillMaxWidth().height(420.dp)) {
                    Image(
                        painter = painterResource(headerImageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .align(Alignment.TopCenter),
                        contentScale = ContentScale.Crop
                    )

                    // Return-home chip (top-left)
                    RoundChipSmall(
                        iconRes = HmR.drawable.return_home,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(14.dp),
                        onClick = onBack
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .align(Alignment.BottomCenter)
                            .shadow(14.dp, RoundedCornerShape(16.dp), clip = false),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            // Address row: title + location chip (right)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "Johannesburg, 1 Road Ubuntu",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                                RoundChipSmall(
                                    iconRes = HmR.drawable.location,
                                    onClick = onOpenMap         // -> Map
                                )
                            }

                            Spacer(Modifier.height(12.dp))

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                SmallField("CHOOSE DATE", "20 Mar - 10h", Modifier.weight(1f))
                                val need = filters?.categories?.joinToString(" • ")
                                    .orEmpty().ifBlank { "Plumber" }
                                SmallField("NEED", need, Modifier.weight(1f))
                            }

                            Spacer(Modifier.height(12.dp))

                            OutlinedTextField(
                                value = "",
                                onValueChange = {},
                                placeholder = { Text("Search location / name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(Modifier.height(16.dp))

                            Button(
                                onClick = { /* search */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .shadow(10.dp, RoundedCornerShape(24.dp), clip = false),
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    "Search",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            /* ===== Orange strip (Favorites | Orders) ===== */
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .height(60.dp)
                        .padding(horizontal = 28.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { showOnlyFavorites = !showOnlyFavorites }
                    ) {
                        Image(painterResource(HmR.drawable.heart), null, Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (showOnlyFavorites) "Favorites (on)" else "Favorites",
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { /* navigate to orders if needed */ }
                    ) {
                        Image(painterResource(HmR.drawable.orders), null, Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Orders", color = Color.White, fontWeight = FontWeight.Medium)
                    }
                }
            }

            /* ===== Header “Teachers 120” + filters icon (options.png) ===== */
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Teachers",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("120", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.weight(1f))
                    Image(
                        painter = painterResource(HmR.drawable.options),
                        contentDescription = "Filters",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onOpenFilters() }
                    )
                }
            }

            /* ===== Cards ===== */
            items(filtered, key = { it.id }) { item ->
                PersonCard(
                    item = item,
                    photoRes = headerImageRes,
                    isFavorite = favorites.contains(item.id),
                    onToggleFavorite = {
                        if (favorites.contains(item.id)) favorites.remove(item.id)
                        else favorites.add(item.id)
                    },
                    onClick = { onOpenProvider(item) }
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

/* ---------------- helpers ---------------- */

@Composable
private fun RoundChipSmall(
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.95f))
            .shadow(6.dp, CircleShape, clip = false)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(painterResource(iconRes), contentDescription = null, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun SmallField(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF5F6F8))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(label, color = Color(0xFF8F9399), fontSize = 12.sp)
        Spacer(Modifier.height(2.dp))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PersonCard(
    item: ProviderUi,
    @DrawableRes photoRes: Int,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp), clip = false),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Column {
            // cover + heart toggle
            Box {
                Image(
                    painter = painterResource(photoRes),
                    contentDescription = item.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.92f))
                        .align(Alignment.TopEnd)
                        .clickable { onToggleFavorite() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(HmR.drawable.heart),
                        contentDescription = "Fav",
                        modifier = Modifier.size(18.dp),
                        alpha = if (isFavorite) 1f else 0.45f
                    )
                }
            }

            // name/role price line
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        item.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text("Johannesburg", color = Color(0xFF8F9399), fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(item.role, color = Color(0xFF8F9399), fontSize = 12.sp)
                    Spacer(Modifier.height(6.dp))
                    Text("$ ${item.pricePerHour}/h", style = MaterialTheme.typography.titleMedium)
                }
            }

            Divider()

            // rating • distance • price row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(HmR.drawable.onestar),
                        contentDescription = "rating",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("${item.rating}")
                }
                Spacer(Modifier.width(18.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF8F9399))
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("${item.distanceMeters} m")
                }
                Spacer(Modifier.weight(1f))
                Text("$ ${item.pricePerHour}/h", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

/* ---------------- model ---------------- */
data class ProviderUi(
    val id: String,
    val name: String,
    val role: String,
    val pricePerHour: Int,
    val rating: Float,
    val distanceMeters: Int
)
