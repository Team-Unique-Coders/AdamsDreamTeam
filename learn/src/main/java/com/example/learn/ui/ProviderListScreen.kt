package com.example.learn.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.project.common_utils.BackArrowIcon
import com.project.common_utils.CircularImageHolderDrawable
import com.project.common_utils.OrangeButton
import com.project.common_utils.ReviewStars
import com.project.common_utils.SearchField

data class ProviderUI(
    val id: String,
    val name: String,
    val subject: String, // e.g., "Plumber • Carpenter"
    val level: String,
    val pricePerHour: Int,     // $/h
    val rating: Float,         // 0..5
    val distanceMeters: Int,   // meters
    val avatarRes: Int? = null
)

@Composable
fun LearnProviderListScreen(
    onBack: () -> Unit,
    onOpenProvider: (ProviderUI) -> Unit,
    onOpenFilters: () -> Unit,
    filters: FiltersState? = null
) {
    var query by remember { mutableStateOf("") }

    val all = remember {
        listOf(
            ProviderUI("1", "Jessy Jones", "English", "College", 15, 4.8f, 500, avatarRes = null),
            ProviderUI(
                "2",
                "Jenny Jones",
                "Math",
                "Middle School",
                22,
                4.6f,
                900,
                avatarRes = null
            ),
            ProviderUI(
                "3",
                "Jean Down",
                "Science",
                "Elementary School",
                12,
                4.2f,
                450,
                avatarRes = null
            ),
            ProviderUI("4", "John Craft", "Art", "High School", 30, 4.9f, 1500, avatarRes = null),
        )
    }

    val visible = remember(query, filters, all) {
        all.asSequence()
            // text query on name or role
            .filter { p ->
                query.isBlank() ||
                        p.name.contains(query, ignoreCase = true) ||
                        p.subject.contains(query, ignoreCase = true)
            }
            // filters (if present)
            .filter { p ->
                if (filters == null) true else {
                    val priceOk = p.pricePerHour <= filters.pricePerHourMax
                    val ratingOk = p.rating >= filters.minRating
                    val distanceOk = p.distanceMeters <= filters.maxDistanceMeters
                    val categoryOk = if (filters.categories.isEmpty()) true
                    else filters.categories.any { cat ->
                        p.subject.contains(cat, ignoreCase = true)
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
            Text("Tutors", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onOpenFilters) {
                Icon(imageVector = Icons.Filled.FilterList, contentDescription = "Filters")
            }
        }

        Spacer(Modifier.height(12.dp))

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
                val needText = if (catHint.isBlank()) "Need  Tutor" else "Need  $catHint"
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
                LearnProviderCard(item = item, onClick = { onOpenProvider(item) })
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun LearnProviderCard(
    item: ProviderUI,
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
                Text(item.subject, style = MaterialTheme.typography.bodyMedium)
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