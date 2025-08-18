package com.example.bank.presentation.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankStartModeScreen(
    onUseDemo: () -> Unit,
    onConnectSimulated: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Get started") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Choose how you want to try Bank in I Click I Pay.\nThis is a demo — no real bank data.",
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onUseDemo
            ) {
                Text("Use demo accounts")
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onConnectSimulated
            ) {
                Text("Connect a bank (simulated)")
            }
        }
    }
}
