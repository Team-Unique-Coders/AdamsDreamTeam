package com.example.learn.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.project.common_utils.R
import com.project.common_utils.components.MapComponent

@Composable
fun LearnMapScreen(onBack: () -> Unit, onOpenProvider: (ProviderUI) -> Unit) {
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
                avatarRes = com.example.learn.R.drawable.provider_img
            ),
            ProviderUI(
                "2",
                "Jenny Jones",
                "Math",
                "Middle School",
                22,
                4.6f,
                900,
                avatarRes = com.example.learn.R.drawable.provider_img
            ),
            ProviderUI(
                "3",
                "Jean Down",
                "Science",
                "Elementary School",
                12,
                4.2f,
                450,
                avatarRes = com.example.learn.R.drawable.provider_img
            ),
            ProviderUI(
                "4",
                "John Craft",
                "Art",
                "High School",
                30,
                4.9f,
                1500,
                avatarRes = com.example.learn.R.drawable.provider_img
            ),
        )
    }

    Box(Modifier.fillMaxSize().safeContentPadding()) {
        MapComponent(latitude = -6.397, longitude = 106.822, zoom = 14.0, isShowButton = true) {

        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                SmallFloatingActionButton(
                    onClick = onBack,
                    containerColor = Color.White,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = colorResource(R.color.orange)
                    )
                }
                SmallFloatingActionButton(
                    onClick = { /* open filters */ },
                    containerColor = Color.White
                ) {
                    Icon(
                        Icons.Outlined.Menu,
                        contentDescription = "Filter",
                        tint = colorResource(R.color.orange)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            TextField(
                value = query,
                onValueChange = { query = it },
                singleLine = true,
                placeholder = { Text("Search") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }
        FloatingActionButton(
            onClick = { /* TODO: animate camera to user's location */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color.White
        ) {
            Icon(Icons.Filled.LocationOn, contentDescription = "My location", tint = colorResource(R.color.orange))

        }
    }
}