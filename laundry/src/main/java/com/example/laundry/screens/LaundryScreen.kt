package com.example.laundry.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.laundry.components.HeadingText
import com.example.laundry.components.StackedImages
import com.example.laundry.navigation.LaundryDestinations
import com.project.common_utils.components.OrangeButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaundryScreen(
    onOpen: (String) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onOpen("home") }) { // <- change "home" if needed
                Icon(Icons.Filled.Home, contentDescription = "Go to Home")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F8F8)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))
            StackedImages()
            HeadingText()

            Spacer(modifier = Modifier.height(screenHeight * 0.1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                OrangeButton(
                    onClick = { onOpen(LaundryDestinations.HOME)  }, // example: navigate elsewhere in app
                    text = "Let's go"
                )
                OrangeButton(
                    onClick = { onOpen(LaundryDestinations.HOME)  }, // example: navigate elsewhere in app
                    text = "Let's go"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LaundryScreenPreview() {
    LaundryScreen()
}
