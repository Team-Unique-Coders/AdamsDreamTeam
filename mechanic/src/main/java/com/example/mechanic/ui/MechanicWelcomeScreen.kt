package com.example.mechanic.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.example.mechanic.R as MeR

private val Orange = Color(0xFFFF7A00)
private val TitleColor = Color(0xFF192032)
private val BodyColor  = Color(0xFF8F9399)

@Composable
fun MechanicWelcomeScreen(
    onLetsGo: () -> Unit,
    onBack: () -> Unit,
    @DrawableRes heroResId: Int = MeR.drawable.mechanic_hero
) {
    Surface(color = Color(0xFFF5F6F8)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(36.dp).clip(CircleShape)
                ) {
                    Icon(
                        painter = painterResource(MeR.drawable.ic_arrow_back),
                        contentDescription = "Back",
                        tint = Orange
                    )
                }
                Spacer(Modifier.weight(1f))
            }

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

            Text("Mechanic", color = TitleColor, fontSize = 28.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)

            Spacer(Modifier.height(14.dp))

            Text(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis lobortis sit amet odio in egestas. Pellen tesque ultricies justo.",
                color = BodyColor,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onLetsGo,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Orange, contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .navigationBarsPadding()
                    .padding(bottom = 18.dp)
            ) {
                Text("Let’go", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
