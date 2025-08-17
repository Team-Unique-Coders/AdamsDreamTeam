package com.example.handyman.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.project.common_utils.BackArrowIcon
import com.project.common_utils.CircularImageHolderDrawable
import com.project.common_utils.OrangeButton
import com.project.common_utils.ReviewStars
import com.project.common_utils.SearchField

// import FiltersState from your Filters screen file
import com.example.handyman.ui.FiltersState

data class ProviderUi(
    val id: String,
    val name: String,
    val role: String,          // e.g., "Plumber • Carpenter"
    val pricePerHour: Int,     // $/h
    val rating: Float,         // 0..5
    val distanceMeters: Int,   // meters
    val avatarRes: Int? = null
)

@Composable
fun ProviderListScreen(
    onBack: () -> Unit,
    onOpenProvider: (ProviderUi) -> Unit,
    onOpenFilters: () -> Unit,
    filters: FiltersState? = null     // <-- NEW: optional filters from nav
) {
    var query by remember { mutableStateOf("") }

    // mock data
    val all = remember {
        listOf(
            ProviderUi("1", "Jessy Jones", "Plumber • Carpenter", 15, 4.8f, 500, avatarRes = null),
            ProviderUi("2", "Jenny Jones", "Electrician",         22, 4.6f, 900, avatarRes = null),
            ProviderUi("3", "Jean Down",   "Painter",              12, 4.2f, 450, avatarRes = null),
            ProviderUi("4", "John Craft",  "Carpenter",            30, 4.9f, 1500, avatarRes = null),
        )
    }

    // apply search + filters
    val visible = remember(query, filters, all) {
        all.asSequence()
            // text query on name or role
            .filter { p ->
                query.isBlank() ||
                        p.name.contains(query, ignoreCase = true) ||
                        p.role.contains(query, ignoreCase = true)
            }
            // filters (if present)
            .filter { p ->
                if (filters == null) true else {
                    val priceOk = p.pricePerHour <= filters.pricePerHourMax
                    val ratingOk = p.rating >= filters.minRating
                    val distanceOk = p.distanceMeters <= filters.maxDistanceMeters
                    val categoryOk = if (filters.categories.isEmpty()) true
                    else filters.categories.any { cat ->
                        p.role.contains(cat, ignoreCase = true)
                    }
                    priceOk && ratingOk && distanceOk && categoryOk
                }
            }
            .toList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            BackArrowIcon(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text("Handymen", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onOpenFilters) {
                Icon(imageVector = Icons.Filled.FilterList, contentDescription = "Filters")
            }
        }

        Spacer(Modifier.height(12.dp))

        // Top hero card — simple colored box placeholder
        Card(elevation = CardDefaults.cardElevation(4.dp), modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(12.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    "Johannesburg, 1 Road Ubuntu",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                val catHint = filters?.categories?.joinToString(" • ").orEmpty()
                val needText = if (catHint.isBlank()) "Need  Handyman" else "Need  $catHint"
                Text(
                    "Choose date 20 Mar • 10h   •   $needText",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Search
        SearchField(
            value = query,
            onValueChange = { query = it },
            placeholder = "Search location / name",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        Divider()

        // List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(visible, key = { it.id }) { item ->
                ProviderCard(item = item, onClick = { onOpenProvider(item) })
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ProviderCard(
    item: ProviderUi,
    onClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.avatarRes != null) {
                CircularImageHolderDrawable(
                    drawableResId = item.avatarRes,
                    description = item.name,
                    size = 56.dp,
                    borderWidth = 0.dp,
                    elevation = 0.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(MaterialTheme.shapes.large),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium)
                Text(item.role, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    var tmp by remember { mutableStateOf(item.rating) }
                    ReviewStars(rating = tmp, onRatingChange = { tmp = it }, size = 18.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("${item.distanceMeters} m")
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("$ ${item.pricePerHour}/h", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                OrangeButton(onClick = onClick, text = "Book")
            }
        }
    }
}
