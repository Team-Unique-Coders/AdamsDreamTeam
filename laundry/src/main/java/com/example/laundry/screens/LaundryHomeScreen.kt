package com.example.laundry.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.laundry.R
import com.example.laundry.data.Provider
import com.example.laundry.data.fake
import com.example.laundry.navigation.LaundryDestinations

/* ---------- tiny helpers ---------- */
private fun providerKey(p: Provider) =
    "%s|%.5f|%.5f".format(java.util.Locale.US, p.name, p.lat, p.lon)

private fun cityFrom(address: String): String =
    address.split(",").map { it.trim() }.getOrNull(1) ?: address

/* ---------- screen ---------- */

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaundryHomeScreen(
    providers: List<Provider>,
    onOpen: (String) -> Unit = {},
    onBack: () -> Unit = {},
) {
    Scaffold(
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            /* hero section */
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.maskgroup),
                        contentDescription = "background",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .align(Alignment.BottomCenter)
                            .background(Color(0xFFFF9800))
                    ) {
                        Row(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconTextButton(com.project.common_utils.R.drawable.heart, "Favorites") { }
                            IconTextButton(R.drawable.order, "Orders") {
                                onOpen(LaundryDestinations.VIEW_ORDERS)
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .align(Alignment.Center)
                            .padding(horizontal = 20.dp)
                            .offset(y = 30.dp)
                            .shadow(16.dp, RoundedCornerShape(16.dp), clip = false)
                            .background(Color.White, RoundedCornerShape(16.dp))
                    ) {
                        WhiteBoxValue(onOpen)
                    }
                }
            }

            /* header row */
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("House Cleaner", color = Color.Black)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("${providers.size}", color = Color.LightGray)
                    }
                    IconButton(onClick = { onOpen(LaundryDestinations.FILTER) }) {
                        Icon(
                            painter = painterResource(id = com.project.common_utils.R.drawable.options),
                            contentDescription = "Options",
                            modifier = Modifier.size(24.dp),
                            tint = Color(0xFFFF9800)
                        )
                    }
                }
            }

            /* REAL provider cards */
            items(
                items = providers,
                key = { providerKey(it) }
            ) { p ->
                ProviderCard(
                    provider = p,
                    onOpen = onOpen
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

/* ---------- pieces ---------- */

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
fun WhiteBoxValue(
    onOpen: (String) -> Unit
) {
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
                onClick = {onOpen(LaundryDestinations.MAPSCREEN)}
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = "Options",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFFFF9800)
                )
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WhiteBoxValueColumn("CHOOSE DATES", "20 Mar - 10h")
            VerticalDivider(Modifier.height(60.dp))
            WhiteBoxValueColumn("KG", "5")
            VerticalDivider(Modifier.height(60.dp))
            WhiteBoxValueColumn("DRY", "2")
            VerticalDivider(Modifier.height(60.dp))
            WhiteBoxValueColumn("IRONING", "Yes")
        }
        HorizontalDivider()

        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Search location/Name")
                Icon(
                    painter = painterResource(com.project.common_utils.R.drawable.search),
                    contentDescription = "searchIcon",
                    modifier = Modifier.size(18.dp)
                )
            }
            Button(
                onClick = { /* TODO search */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800),
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
private fun ProviderCard(
    provider: Provider,
    onOpen: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(horizontal = 20.dp)
            .offset(y = 30.dp)
            .clickable(onClick = { onOpen(LaundryDestinations.details(providerKey(provider))) })
            .shadow(16.dp, RoundedCornerShape(16.dp), clip = false)
            .background(Color.White, RoundedCornerShape(16.dp))
    ) {
        Column(Modifier.fillMaxSize()) {
            // Photo
            AsyncImage(
                model = provider.photoUrl,
                contentDescription = provider.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            )

            // Text info
            Column(Modifier.padding(12.dp)) {
                Text(provider.name, color = Color.Black, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(3.dp))
                Text(cityFrom(provider.address), color = Color.LightGray, fontSize = 13.sp)
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
                CardDetailsValues(com.project.common_utils.R.drawable.stars, "${provider.rating}")
                CardDetailsValues(R.drawable.route, "${provider.distanceMiles} mi")
                Text(provider.priceText, fontSize = 12.sp)
            }
        }
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

@Preview(showBackground = true, widthDp = 360, heightDp = 840)
@Composable
fun LaundryHomeScreenPreview() {
    MaterialTheme {
        LaundryHomeScreen(providers = fake)
    }
}
