package com.example.bank

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankScreen(
    onBackToHome: () -> Unit = {}   // app passes this in
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Bank") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onBackToHome) {
                Icon(Icons.Filled.Home, contentDescription = "Go to Home")
            }
        }
    ) { padding ->
        Text(
            text = "Bank Feature Home",
            modifier = Modifier.padding(padding)
        )
    }
}
