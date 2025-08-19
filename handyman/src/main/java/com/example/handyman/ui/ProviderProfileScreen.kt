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
import com.project.common_utils.components.OrangeButton

@Composable
fun ProviderProfileScreen(
    onBack: () -> Unit,
    onTakeAppointment: () -> Unit,
    @DrawableRes avatarRes: Int = HmR.drawable.profilepicture
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFF1))
    ) {
        /* ---------- HEADER MAP (real map in the background) ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .align(Alignment.TopCenter)
        ) {
            MapComponent() // from common_utils (osmdroid compose)
        }

        /* Back chip like the mock (top-left on top of the map) */
        Surface(
            modifier = Modifier
                .padding(14.dp)
                .size(40.dp)
                .align(Alignment.TopStart),
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 8.dp
        ) { BackArrowIcon(onClick = onBack) }

        /* Simple green pin in the map center (visual detail from mock) */
        Box(
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.TopCenter)
                .offset(y = 90.dp) // roughly mid-map
                .clip(CircleShape)
                .background(Color(0xFF3CCB76))
                .border(3.dp, Color.White, CircleShape)
        )

        /* ---------- FLOATING CARD that overlaps the map ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = 140.dp) // overlap amount
                .align(Alignment.TopCenter)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(18.dp, RoundedCornerShape(16.dp), clip = false),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                // leave space so the avatar sits in the card
                Spacer(Modifier.height(46.dp))

                Column(Modifier.padding(horizontal = 18.dp)) {

                    /* Name centered; rating on left; price on right (like mock) */
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
                            "Jenny Jones",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(Modifier.weight(1f))
                        Text("$ 15/h")
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
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Plumber", style = MaterialTheme.typography.titleMedium)
                            Text("Carpenter", color = Color(0xFF8F9399))
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        OrangeButton(onClick = onTakeAppointment, text = "Take appointment")
                    }
                    Spacer(Modifier.height(18.dp))
                }
            }

            /* ---------- AVATAR centered on card edge ---------- */
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-44).dp) // half inside card
            ) {
                Image(
                    painter = painterResource(avatarRes),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .shadow(8.dp, CircleShape, clip = false),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
