package com.example.handyman.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.common_utils.components.BackArrowIcon
import com.example.handyman.R as HmR

private val BrandOrange = Color(0xFFFF7A00)
private val BodyGrey = Color(0xFF8F9399)

/**
 * Order success / confirmation
 */
@Composable
fun SuccessScreen(
    onGoHome: () -> Unit,
    onBack: () -> Unit = onGoHome,
    title: String = "Order successful",
    message: String = "Your booking has been confirmed.\nWe’ll notify you with any updates.",
    // Use any illustration you like; this default exists in your repo
    @DrawableRes illustrationRes: Int = HmR.drawable.success
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /* back arrow area */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackArrowIcon(onClick = onBack, tint = BrandOrange)
        }

        Spacer(Modifier.height(8.dp))

        /* illustration */
        Image(
            painter = painterResource(illustrationRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 160.dp, max = 220.dp)
                .padding(horizontal = 8.dp)
        )

        Spacer(Modifier.height(16.dp))

        /* title & copy */
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = BodyGrey,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        /* centered orange CTA right below image/text */
        Button(
            onClick = onGoHome,
            modifier = Modifier
                .widthIn(min = 220.dp)
                .height(54.dp)
                .shadow(14.dp, RoundedCornerShape(14.dp), clip = false)
                .clip(RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandOrange,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Text(
                "Order successful",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )
        }

        /* leave free space below */
        Spacer(Modifier.weight(1f))
    }
}
