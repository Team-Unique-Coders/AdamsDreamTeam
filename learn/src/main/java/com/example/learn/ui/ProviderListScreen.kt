package com.example.learn.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learn.R
import com.project.common_utils.components.ButtonIcon
import com.project.common_utils.components.SearchField

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
    onOpenMap: () -> Unit,
    filters: FiltersState? = null
) {
    var query by remember { mutableStateOf("") }

    val all = remember {
        listOf(
            ProviderUI(
                "1",
                "Jessy Jones",
                "English",
                "College",
                15,
                4.8f,
                500,
                avatarRes = R.drawable.provider_img
            ),
            ProviderUI(
                "2",
                "Jenny Jones",
                "Math",
                "Middle School",
                22,
                4.6f,
                900,
                avatarRes = R.drawable.provider_img
            ),
            ProviderUI(
                "3",
                "Jean Down",
                "Science",
                "Elementary School",
                12,
                4.2f,
                450,
                avatarRes = R.drawable.provider_img
            ),
            ProviderUI(
                "4",
                "John Craft",
                "Art",
                "High School",
                30,
                4.9f,
                1500,
                avatarRes = R.drawable.provider_img
            ),
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
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.background_img),
                        contentDescription = "background",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        contentScale = ContentScale.Crop
                    )

                    /*FloatingActionButton(
                        onClick = onBack,
                        modifier = Modifier.padding(16.dp),
                        containerColor = Color.White
                    ) {
                        Icon(
                            Icons.Outlined.Home,
                            contentDescription = "Go to Home",
                            tint = colorResource(com.project.common_utils.R.color.orange)
                        )
                    }*/

                    ButtonIcon(
                        com.project.common_utils.R.drawable.home,
                        onClick = onBack,
                        background = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                colorResource(com.project.common_utils.R.color.orange)
                            )
                    ) {
                        Row(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconTextButton(
                                iconId = com.project.common_utils.R.drawable.heart,
                                text = "Favorites"
                            ) { }
                            IconTextButton(
                                iconId = com.project.common_utils.R.drawable.order,
                                text = "Orders"
                            ) { }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(270.dp)
                            .align(Alignment.Center)
                            .padding(horizontal = 20.dp)
                            .offset(y = 20.dp)
                            .shadow(16.dp, RoundedCornerShape(16.dp), clip = false)
                            .background(Color.White, RoundedCornerShape(16.dp))
                    ) {
                        WhiteBoxValue(query, onValueChange = { query = it }, onOpenMap)
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Teachers", color = Color.Black)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("${visible.size}", color = Color.LightGray)
                    }
                    IconButton(onOpenFilters) {
                        Icon(
                            painter = painterResource(id = com.project.common_utils.R.drawable.options),
                            contentDescription = "Options",
                            modifier = Modifier.size(24.dp),
                            tint = colorResource(com.project.common_utils.R.color.orange)
                        )
                    }
                }
            }
            // List
            items(visible, key = { it.id }) { item ->
                LearnProviderCard(
                    item = item,
                    onClick = { onOpenProvider(item) })
                Spacer(Modifier.height(12.dp))
            }

        }
    }
}

@Composable
fun LearnProviderCard(
    item: ProviderUI,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(horizontal = 20.dp)
            .offset(y = 30.dp)
            .clickable(onClick = onClick)
            .shadow(16.dp, RoundedCornerShape(16.dp), clip = false)
            .background(Color.White, RoundedCornerShape(16.dp))
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            var imgRes: Int = item.avatarRes ?: com.project.common_utils.R.drawable.profiled
            Image(
                painter = painterResource(imgRes),
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.padding(12.dp)) {
                Text(item.name, color = Color.Black, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    "Johannesburg",
                    color = Color.LightGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CardDetailsValues(com.project.common_utils.R.drawable.stars, "${item.rating}")
                CardDetailsValues(
                    com.project.common_utils.R.drawable.route,
                    "${item.distanceMeters} m"
                )
                Text("$${item.pricePerHour}/h", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun IconTextButton(
    iconId: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .background(Color.Transparent, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = text,
            modifier = Modifier.size(24.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = Color.White, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun WhiteBoxValue(query: String, onValueChange: (String) -> Unit, onOpen: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Johannesburg, 1 Road Ubuntu", fontSize = 15.sp)
            IconButton(
                onClick = onOpen
            ) {
                Icon(
                    painter = painterResource(id = com.project.common_utils.R.drawable.location),
                    contentDescription = "Options",
                    modifier = Modifier.size(24.dp),
                    tint = colorResource(com.project.common_utils.R.color.orange)
                )
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WhiteBoxValueColumn("CHOOSE DATES", "20 Mar - 10h")
            VerticalDivider(Modifier.height(52.dp))
            WhiteBoxValueColumn("LESSON", "English")
            VerticalDivider(Modifier.height(52.dp))
            WhiteBoxValueColumn("COLLEGE", "College")
        }
        HorizontalDivider()

        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SearchField(
                    value = query,
                    onValueChange = onValueChange,
                    placeholder = "Search location / name",
                    modifier = Modifier.fillMaxWidth()
                )
                Icon(
                    painter = painterResource(com.project.common_utils.R.drawable.search),
                    contentDescription = "searchIcon"
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { /* TODO search */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(com.project.common_utils.R.color.orange),
                    contentColor = Color.White
                )
            ) {
                Text("Search", modifier = Modifier.padding(horizontal = 70.dp))
            }

        }
    }
}

@Composable
fun WhiteBoxValueColumn(heading: String, value: String) {
    Column(Modifier.padding(10.dp)) {
        Text(heading, color = Color.LightGray, fontSize = 13.sp)
        Text(value, color = Color.Black, fontSize = 15.sp)
    }
}

@Composable
fun CardDetailsValues(id: Int, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id),
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = Color(0xFFFF9800)
        )
        Spacer(Modifier.width(4.dp))
        Text(value, fontSize = 12.sp)
    }
}