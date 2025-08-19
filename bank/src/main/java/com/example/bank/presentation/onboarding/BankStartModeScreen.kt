// file: com/example/bank/presentation/onboarding/BankStartModeScreen.kt
package com.example.bank.presentation.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.project.common_utils.components.OrangeButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankStartModeScreen(
    onUseDemo: () -> Unit,          // “Link a bank account” CTA
    onConnectSimulated: () -> Unit, // (kept for API compat; not used here)
    onBack: () -> Unit,
    @DrawableRes illustrationRes: Int = com.example.bank.R.drawable.startmode
) {
    val brandOrange = Color(0xFFFF7A1A)

    // Bottom sheet state for “Open a bank account”
    var showOpenSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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

            // Illustration
            Image(
                painter = painterResource(illustrationRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(vertical = 8.dp)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Start with a Checking account to send and receive money. " +
                        "You can link an external bank account anytime.",
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.weight(1f))

            // Primary CTA — stays the same
            OrangeButton(
                onClick = onUseDemo,
                text = "Link a bank account"
            )

            Spacer(Modifier.height(16.dp))

            // Secondary CTA — outlined, does NOT navigate; opens the interactive prompt
            Button(
                onClick = { showOpenSheet = true },
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
                    containerColor = Color.White,
                    contentColor = brandOrange
                ),
                border = BorderStroke(1.dp, brandOrange)
            ) {
                Text("Open a bank account")
            }

            Spacer(Modifier.height(32.dp))
        }
    }

    // ---------- Interactive prompt (Bottom Sheet) ----------
    if (showOpenSheet) {
        ModalBottomSheet(
            onDismissRequest = { showOpenSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon + subtle progress to convey "in progress"
                Icon(
                    imageVector = Icons.Filled.AccountBalance,
                    contentDescription = null,
                    tint = brandOrange,
                    modifier = Modifier.size(42.dp)
                )
                Spacer(Modifier.height(8.dp))
                CircularProgressIndicator(
                    color = brandOrange,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(Modifier.height(16.dp))
                Text(
                    "Account opening is on the way",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "We’re building instant account opening. For now, you can link an existing bank and start using the app.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(20.dp))

                // Dismiss only — no redirect (per your request)
                Button(
                    onClick = { showOpenSheet = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = brandOrange,
                        contentColor = Color.White
                    )
                ) {
                    Text("OK, got it")
                }

                Spacer(Modifier.height(10.dp))
            }
        }
    }
}
