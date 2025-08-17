package com.example.mechanic.ui

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
import com.project.common_utils.*

data class MechanicUi(
    val id: String,
    val name: String,
    val role: String,
    val pricePerHour: Int,
    val rating: Float,
    val distanceMeters: Int,
    val avatarRes: Int? = null
)

@Composable
fun MechanicProviderListScreen(
    onBack: () -> Unit,
    onOpenProvider: (MechanicUi) -> Unit,
    onOpenFilters: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val all = remember {
        listOf(
            MechanicUi("1", "Jessy Jones", "Car • Motorcycle", 15, 4.8f, 500),
            MechanicUi("2", "Jenny Jones", "Car",              15, 4.8f, 450),
            MechanicUi("3", "Jean Down",   "Motorcycle",       12, 4.5f, 450)
        )
    }
    val list = remember(query, all) {
        if (query.isBlank()) all else all.filter {
            it.name.contains(query, true) || it.role.contains(query, true)
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            BackArrowIcon(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text("Mechanics", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onOpenFilters) {
                Icon(Icons.Filled.FilterList, contentDescription = "Filters")
            }
        }

        Spacer(Modifier.height(12.dp))

        // “hero” card like the mock
        Card(elevation = CardDefaults.cardElevation(4.dp), modifier = Modifier.fillMaxWidth()) {
            Column(
                Modifier.fillMaxWidth().height(140.dp)
                    .padding(12.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text("Johannesburg, 1 Road Ubuntu", style = MaterialTheme.typography.titleMedium)
                Text("Choose date 20 Mar • 10h   •   Type Car", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(Modifier.height(12.dp))

        SearchField(
            value = query,
            onValueChange = { query = it },
            placeholder = "Search location / name",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Divider()

        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 8.dp)) {
            items(list, key = { it.id }) { item ->
                MechanicCard(item, onClick = { onOpenProvider(item) })
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun MechanicCard(item: MechanicUi, onClick: () -> Unit) {
    Card(elevation = CardDefaults.cardElevation(2.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (item.avatarRes != null) {
                CircularImageHolderDrawable(item.avatarRes, item.name, size = 56.dp, borderWidth = 0.dp, elevation = 0.dp)
            } else {
                Icon(Icons.Filled.Person, contentDescription = item.name,
                    modifier = Modifier.size(56.dp).clip(MaterialTheme.shapes.large),
                    tint = MaterialTheme.colorScheme.primary)
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
