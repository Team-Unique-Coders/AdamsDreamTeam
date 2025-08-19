package com.example.handyman.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.handyman.R as HmR

private val Orange = Color(0xFFFF7A00)
private val TitleColor = Color(0xFF192032)    // dark navy like the mock
private val BodyColor  = Color(0xFF8F9399)    // subtle gray like the mock

@Composable
fun HandymanWelcomeScreen(
    onLetsGo: () -> Unit,
    onBack: () -> Unit,
    @DrawableRes heroResId: Int = HmR.drawable.handyman_hero
) {
    // Light gray page background (subtle)
    Surface(color = Color(0xFFF5F6F8)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()          // status bar cutout safe
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back arrow row (left)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                ) {
                    Icon(
                        painter = painterResource(HmR.drawable.ic_arrow_back), // your arrow
                        contentDescription = "Back",
                        tint = Orange
                    )
                }
                Spacer(Modifier.weight(1f))
            }

            // Hero artwork (no card; large, centered)
            Image(
                painter = painterResource(heroResId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(220.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(18.dp))

            // Title
            Text(
                "Handyman",
                color = TitleColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(14.dp))

            // Body copy (3 lines like the mock)
            Text(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis lobortis sit amet odio in egestas. Pellen tesque ultricies justo.",
                color = BodyColor,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(Modifier.weight(1f))

            // Primary button
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // soft shadow plate behind the button (just a Surface)
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(18.dp),
                    tonalElevation = 0.dp,
                    shadowElevation = 18.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp) // keep glow visible
                        .height(1.dp)                 // trick to create a soft shadow under
                ) {}

                Button(
                    onClick = onLetsGo,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(vertical = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)                // ensure ample height so text is visible
                        .navigationBarsPadding()      // keep clear of gesture bar
                        .padding(bottom = 18.dp)      // extra space like the mock
                ) {
                    Text(
                        text = "Let’go",              // exactly like the screenshot
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
