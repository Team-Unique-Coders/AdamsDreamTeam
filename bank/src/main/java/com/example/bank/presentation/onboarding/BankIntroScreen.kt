package com.example.bank.presentation.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.common_utils.components.OrangeButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankIntroScreen(
    onContinue: () -> Unit,
    onExploreDemo: () -> Unit,
    onClose: (() -> Unit)? = null,
    @DrawableRes illustrationRes: Int = com.example.bank.R.drawable.bankintro
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bank") },            // ← show title in the toolbar
                navigationIcon = {
                    if (onClose != null) {
                        IconButton(onClick = onClose) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            Image(
                painter = painterResource(illustrationRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .padding(top = 8.dp, bottom = 24.dp)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Bank",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                        "Duis lobortis sit amet odio in egestas. Pellen tesque ultricies justo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.weight(1f))

            OrangeButton(onClick = onContinue, text = "Let’go")
            Spacer(Modifier.height(24.dp))
        }
    }
}
