package com.example.handyman.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

/* ---------- palette tuned to the mock ---------- */
private val ORANGE      = Color(0xFFFF7A00)
private val TITLE       = Color(0xFF1D2B39)
private val SUBTLE      = Color(0xFF8F9399)
private val BORDER      = Color(0xFFE5E7EB)
private val FIELD_FILL  = Color.White
private val PAGE_BG     = Color(0xFFF6F7F9)

/* ---------- screen ---------- */
@Composable
fun ProviderListScreen(
    onBack: () -> Unit,
    onOpenProvider: (ProviderUi) -> Unit,
    onOpenFilters: () -> Unit,
    onOpenMap: () -> Unit,
    filters: FiltersState? = null,
    @DrawableRes bannerResId: Int? = null
) {
    var query by remember { mutableStateOf("") }
    val favorites = remember { mutableStateListOf<String>() }
    var showOnlyFavorites by remember { mutableStateOf(false) }

    val all = remember {
        listOf(
            ProviderUi("1", "Jessy Jones", "Plumber  •  Carpenter", 15, 4.8f, 500),
            ProviderUi("2", "Jenny Jones", "Electrician",            22, 4.6f, 900),
            ProviderUi("3", "Jean Down",   "Painter",                12, 4.2f, 450),
            ProviderUi("4", "John Craft",  "Carpenter",              30, 4.9f, 1500)
        )
    }

    val filtered = remember(query, all, filters, favorites, showOnlyFavorites) {
        all
            .filter { if (showOnlyFavorites) favorites.contains(it.id) else true }
            .filter { if (query.isBlank()) true else it.name.contains(query, true) || it.role.contains(query, true) }
            .filter { p ->
                filters?.let {
                    val priceOk = p.pricePerHour <= it.pricePerHourMax
                    val ratingOk = p.rating >= it.minRating
                    val distanceOk = p.distanceMeters <= it.maxDistanceMeters
                    val catOk = it.categories.isEmpty() || it.categories.any { c -> p.role.contains(c, true) }
                    priceOk && ratingOk && distanceOk && catOk
                } ?: true
            }
    }

    val banner = bannerResId?.takeIf { it != 0 } ?: HmR.drawable.mechanic_list

    Scaffold(containerColor = PAGE_BG) { inner ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            /* ---------------- Banner + top-left home chip ---------------- */
            item {
                Box(Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(banner),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp),
                        contentScale = ContentScale.Crop
                    )

                    Surface(
                        onClick = onBack,
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 10.dp,
                        modifier = Modifier
                            .padding(start = 12.dp, top = 12.dp)
                            .size(40.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(HmR.drawable.return_home),
                                contentDescription = "Home",
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }

            /* ---------------- Floating white card (matches grid) ---------------- */
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(12.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .offset(y = (-36).dp)
                ) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        // Address + round orange chip on right
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Johannesburg, 1 Road Ubuntu",
                                color = TITLE,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                onClick = onOpenMap,
                                shape = CircleShape,
                                color = Color.White,
                                shadowElevation = 8.dp,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Image(
                                        painter = painterResource(HmR.drawable.location),
                                        contentDescription = "Location",
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // Grid container
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, BORDER, RoundedCornerShape(8.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)   // bumped from 56.dp
                            ) {
                                GridCell(
                                    header = "CHOOSE DATE",
                                    value = "20 Mar - 10h",
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(topStart = 8.dp))
                                )
                                Box(
                                    Modifier
                                        .width(1.dp)
                                        .fillMaxHeight()
                                        .background(BORDER)
                                )
                                GridCell(
                                    header = "NEED",
                                    value = "Plumber",
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(topEnd = 8.dp))
                                )
                            }

                            // search row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp) // bumped from 56.dp
                                    .border(
                                        width = 0.5.dp,
                                        color = BORDER,
                                        shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                                    )
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    value = query,
                                    onValueChange = { query = it },
                                    placeholder = { Text("Search location / name", color = SUBTLE) },
                                    trailingIcon = {
                                        Image(
                                            painter = painterResource(HmR.drawable.search),
                                            contentDescription = "Search",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    },
                                    singleLine = true,
                                    shape = RoundedCornerShape(0.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        disabledBorderColor = Color.Transparent,
                                        errorBorderColor = Color.Transparent,
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedContainerColor = FIELD_FILL,
                                        unfocusedContainerColor = FIELD_FILL
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        // Big pill button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(20.dp, RoundedCornerShape(24.dp), clip = false)
                        ) {
                            Button(
                                onClick = { /* search */ },
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ORANGE),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                            ) {
                                Text("Search", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            /* ---------------- Orange Favorites / Orders strip ---------------- */
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ORANGE)
                        .padding(horizontal = 32.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { showOnlyFavorites = !showOnlyFavorites }
                    ) {
                        Image(painter = painterResource(HmR.drawable.heart), contentDescription = "Favorites", modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Text(if (showOnlyFavorites) "Favorites (on)" else "Favorites", color = Color.White, fontWeight = FontWeight.Medium)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { /* orders */ }
                    ) {
                        Image(painter = painterResource(HmR.drawable.orders), contentDescription = "Orders", modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("Orders", color = Color.White, fontWeight = FontWeight.Medium)
                    }
                }
            }

            /* ---------------- Teachers + filter chip ---------------- */
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Teachers", color = TITLE, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                    Spacer(Modifier.width(6.dp))
                    Text("120", color = SUBTLE)
                    Spacer(Modifier.weight(1f))
                    Surface(
                        onClick = onOpenFilters,
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 8.dp,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(painter = painterResource(HmR.drawable.options), contentDescription = "Filters", modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            /* ---------------- Cards ---------------- */
            items(filtered, key = { it.id }) { p ->
                ProviderCard(
                    provider = p,
                    isFavorite = favorites.contains(p.id),
                    onToggleFavorite = {
                        if (favorites.contains(p.id)) favorites.remove(p.id) else favorites.add(p.id)
                    },
                    onClick = { onOpenProvider(p) }
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

/* ================= pieces ================= */

@Composable
private fun GridCell(
    header: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(FIELD_FILL)
            .padding(horizontal = 12.dp, vertical = 10.dp), // bumped from 8.dp
        verticalArrangement = Arrangement.Center
    ) {
        Text(header, color = SUBTLE, fontSize = 12.sp, lineHeight = 14.sp)
        Spacer(Modifier.height(2.dp))
        Text(value, color = TITLE, fontSize = 14.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun ProviderCard(
    provider: ProviderUi,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(HmR.drawable.handyman_banner),
                    contentDescription = provider.name,
                    modifier = Modifier.fillMaxWidth().height(140.dp),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    onClick = onToggleFavorite,
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(HmR.drawable.heart),
                            contentDescription = "Fav",
                            modifier = Modifier.size(18.dp),
                            alpha = if (isFavorite) 1f else 0.45f
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(Modifier.weight(1f)) {
                    Text(provider.name, color = TITLE, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("Johannesburg", color = SUBTLE, fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(provider.role, color = SUBTLE, fontSize = 12.sp)
                    Spacer(Modifier.height(6.dp))
                    Text("$ ${provider.pricePerHour}/h", color = TITLE, fontWeight = FontWeight.SemiBold)
                }
            }

            Divider(color = BORDER)

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(HmR.drawable.onestar), contentDescription = "rating", modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("${provider.rating}", color = TITLE)
                }
                Spacer(Modifier.width(18.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(8.dp).clip(CircleShape).background(SUBTLE))
                    Spacer(Modifier.width(6.dp))
                    Text("${provider.distanceMeters / 1000f} km", color = SUBTLE)
                }
                Spacer(Modifier.weight(1f))
                Text("$ ${provider.pricePerHour}/h", color = TITLE, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
