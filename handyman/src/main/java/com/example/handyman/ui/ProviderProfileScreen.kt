package com.example.handyman.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.handyman.R as HmR
import com.project.common_utils.components.MapComponent
import com.project.common_utils.components.BackArrowIcon

@Composable
fun ProviderProfileScreen(
    onBack: () -> Unit,
    onTakeAppointment: () -> Unit,
    providerName: String = "Jenny Jones",
    @DrawableRes avatarRes: Int = HmR.drawable.profilepicture
) {
    val (rateLabel, rolePrimary, roleSecondary) = when {
        providerName.contains("Jean", ignoreCase = true)  -> Triple("$ 12/h", "Painter",  "Carpenter")
        providerName.contains("Jenny", ignoreCase = true) -> Triple("$ 15/h", "Plumber",  "Carpenter")
        else                                               -> Triple("$ 15/h", "Handyman", "Carpenter")
    }

    val mapPeekHeight = 240.dp
    val avatarSize    = 88.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFF1))
    ) {
        /* ---------- HEADER MAP ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(mapPeekHeight)
                .align(Alignment.TopCenter)
        ) {
            MapComponent()
        }

        /* Back chip */
        Surface(
            modifier = Modifier
                .padding(14.dp)
                .size(40.dp)
                .align(Alignment.TopStart),
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 8.dp
        ) { BackArrowIcon(onClick = onBack) }

        /* Green pin */
        Box(
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.TopCenter)
                .offset(y = (mapPeekHeight / 2) - 20.dp)
                .clip(CircleShape)
                .background(Color(0xFF3CCB76))
                .border(3.dp, Color.White, CircleShape)
        )

        /* ---------- FLOATING CARD ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = mapPeekHeight - (avatarSize / 2) + 16.dp)
                .align(Alignment.TopCenter)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(18.dp, RoundedCornerShape(16.dp), clip = false),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Spacer(Modifier.height(46.dp))

                Column(Modifier.padding(horizontal = 18.dp)) {

                    /* rating | name | price row */
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(HmR.drawable.onestar),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("4.8")
                        }
                        Spacer(Modifier.weight(1f))
                        Text(
                            providerName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(Modifier.weight(1f))
                        Text(rateLabel)
                    }

                    Spacer(Modifier.height(14.dp))
                    Text(
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis lobortis sit amet odio in egestas. Pellen tesque ultricies justo.",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                        lineHeight = 18.sp
                    )

                    Spacer(Modifier.height(14.dp))
                    Divider()

                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(HmR.drawable.location),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                "28 Broad Street",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Text("Johannesburg", color = Color(0xFF8F9399))
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    Divider()

                    Spacer(Modifier.height(10.dp))
                    Column {
                        Text(rolePrimary, style = MaterialTheme.typography.titleMedium)
                        Text(roleSecondary, color = Color(0xFF8F9399))
                    }

                    Spacer(Modifier.height(18.dp))

                    /* ---- ORANGE CTA ---- */
                    Button(
                        onClick = onTakeAppointment,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .shadow(12.dp, RoundedCornerShape(12.dp), clip = false),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF7A00), // <-- Hardcoded orange
                            contentColor   = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Text(
                            "Take appointment",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Spacer(Modifier.height(18.dp))
                }
            }

            /* Avatar */
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-avatarSize / 2))
            ) {
                Image(
                    painter = painterResource(avatarRes),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(avatarSize)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .shadow(8.dp, CircleShape, clip = false),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
