// file: com/example/bank/presentation/onboarding/BankStartModeScreen.kt
package com.example.bank.presentation.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import com.project.common_utils.components.OrangeButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankStartModeScreen(
    onUseDemo: () -> Unit,          // Open Checking account
    onConnectSimulated: () -> Unit, // Link a bank account (later)
    onBack: () -> Unit,
    @DrawableRes illustrationRes: Int = com.example.bank.R.drawable.startmode
) {
    val brandOrange = Color(0xFFFF7A1A)

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Choose your first account") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    navigationIconContentColor = Color.Black,
                    titleContentColor = Color.Black
                ),
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // Larger illustration
            Image(
                painter = painterResource(illustrationRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // bigger height for full look
                    .padding(vertical = 8.dp)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Start with a Checking account to send and receive money. " +
                        "You can link an external bank account anytime.",
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(Modifier.weight(1f))

            // Primary CTA
            OrangeButton(
                onClick = onUseDemo,
                text = "Open Checking account"
            )

            Spacer(Modifier.height(16.dp))

            // Secondary CTA - same size as OrangeButton
            Button(
                onClick = onConnectSimulated,
                modifier = Modifier
                    .width(220.dp)
                    .height(60.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(6.dp)),
                shape = RoundedCornerShape(6.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,   // White background
                    contentColor = brandOrange      // Orange text
                ),
                border = BorderStroke(1.dp, brandOrange)
            ) {
                Text("Link a bank account")
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
