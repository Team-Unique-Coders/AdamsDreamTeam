package com.example.tinder.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tinder.R
import androidx.compose.foundation.BorderStroke


@Composable
fun WelcomeScreen(onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.adamintro),
            contentDescription = "Tinder-style Logo",
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Meet",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Text(
            text = " Lorem ipsum dolor sit amet, " +
                    "consectetur adipiscing elit. " +
                    "Duis lobortis sit amet odio in " +
                    "egestas. Pellen tesque ultricies " +
                    "justo.",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraLight,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))

        Button(
            //navigation
            onClick = { onNext()},
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Let's go",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun AddProfilePicture(onPhoto: () -> Unit, onPicture: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.adamsecondscreen),
            contentDescription = "Tinder-style Logo",
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Add Your Profile Picture",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Add a profile picture to help people find you",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraLight,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f)) // push buttons to bottom

        // Top button
        Button(
            onClick = { onPhoto() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Add a picture",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onPicture() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Take a picture",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }

}


@Composable
fun TakePhoto(onPicture: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.android),
            contentDescription = "Tinder-style Logo",
            modifier = Modifier
                .size(500.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onPicture() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Done, // built-in Material icon
                contentDescription = "Take Photo",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }


}

@Composable
fun EnableGeolocation(onTutorial: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.adamgeo),
            contentDescription = "Tinder-style Logo",
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Enable Geolocation",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Text(
            text = "To propose profiles near you," +
                    "you must activate the location service on your device.",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraLight,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f)) // pushes button to bottom

        Button(
            onClick = { onTutorial() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Activate",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }


}

@Composable
fun Tutorial(onMain: () -> Unit) {
    val list = listOf(
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Return ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraLight)) {
                append("To profile")
            }
        } to R.drawable.returnemote,
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("No Favorite ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraLight)) {
                append("The profile will not appear")
            }
        } to R.drawable.sadadam,
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Like ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraLight)) {
                append("If it's mutual, you can talk together")
            }
        } to R.drawable.happyadam,
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Super Like ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraLight)) {
                append("Indicate visually that you are interested")
            }
        } to R.drawable.love,
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Boost ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraLight)) {
                append("Be top profile for 30mins")
            }
        } to R.drawable.adamboost
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // 🔹 Only Image (removed TUTORIAL text)
                Image(
                    painter = painterResource(id = R.drawable.adamtut),
                    contentDescription = "Main Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            items(list) { (label, imageRes) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = label,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }

        Button(
            onClick = { onMain() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Discover your profile",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}






//07
@Composable
fun ProfileFullScreen(
    photo: Int,
    name: String,
    age: Int,
    city: String,
    navController: NavController
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = photo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = name, style = MaterialTheme.typography.titleLarge)
                Text(text = "$age years old", style = MaterialTheme.typography.bodyMedium)
                Text(text = city, style = MaterialTheme.typography.bodyMedium)
            }
        }

        // Back button
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
    }
}


