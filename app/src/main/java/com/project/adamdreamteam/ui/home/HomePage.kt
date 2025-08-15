package com.project.adamdreamteam.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.adamdreamteam.R
import com.project.adamdreamteam.navigation.Routes

data class FeatureItem(val title: String, val iconRes: Int, val route: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(onOpen: (String) -> Unit) {

    val featureItems: List<FeatureItem> = listOf(
        FeatureItem("Uber", R.drawable.uber_icon, Routes.UBER),
        FeatureItem("Tinder", R.drawable.tinder_icon, Routes.TINDER),
        FeatureItem("Delivery", R.drawable.delivery_icon, Routes.DELIVERY),
        FeatureItem("Learn", R.drawable.learn_icon, Routes.LEARN),
        FeatureItem("Chat", R.drawable.bank_icon, Routes.CHAT),
        FeatureItem("Doctor", R.drawable.doctor_icon, Routes.DOCTOR),
        FeatureItem("Laundry", R.drawable.laundry_icon, Routes.LAUNDRY),
        FeatureItem("Eat", R.drawable.eat_icon, Routes.EAT),
        FeatureItem("Hotel", R.drawable.hotel_icon, Routes.HOTEL),
        FeatureItem("Handyman", R.drawable.handyman_icon, Routes.HANDYMAN),
        FeatureItem("Mechanic", R.drawable.mechanic_icon, Routes.MECHANIC),
        FeatureItem("Bank", R.drawable.bank_icon, Routes.BANK)
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("I Click I Pay") }) }
    ) { inner ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .padding(inner)
                .fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(featureItems) { feature ->
                Column(
                    modifier = Modifier
                        .width(76.dp)
                        .clickable { onOpen(feature.route) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(tonalElevation = 2.dp, shape = MaterialTheme.shapes.large) {
                        Box(
                            Modifier
                                .size(56.dp)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(feature.iconRes),
                                contentDescription = feature.title
                            )
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        feature.title,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }
            }
        }
    }
}
