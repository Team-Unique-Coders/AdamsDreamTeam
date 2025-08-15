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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tinder.R


//First Screen
@Preview
@Composable
fun WelcomeScreen() {
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
    }
}

//Second Screen
@Preview
@Composable
fun AddProfilePicture() {
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
    }

}

//Third Screen
@Preview
@Composable
fun TakePhoto() {
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
    }

}

@Preview(showBackground = true)
@Composable
fun CompleteProfile() {
    var topText by remember { mutableStateOf("") }
    var middleText by remember { mutableStateOf("") }
    var bottomText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = topText,
            onValueChange = { topText = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = middleText,
            onValueChange = { middleText = it },
            label = { Text("City") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = bottomText,
            onValueChange = { bottomText = it },
            label = { Text("Date") },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .weight(0.1f),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Text(
            text = "Upload your photos",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(12.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(3) {
                Card(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.android),
                        contentDescription = "Photo ${it + 1}",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}



@Preview
@Composable
fun EnableGeolocation() {
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
    }
}
@Preview
@Composable
fun Tutorial(){


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
        } to R.drawable.nolike,
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Like ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraLight)) {
                append("If it's mutual, you can talk together")
            }
        } to R.drawable.like,
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Super Like ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraLight)) {
                append("Indicate visually that you are interested")
            }
        } to R.drawable.superlike,
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Boost ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraLight)) {
                append("Be top profile for 30mins")
            }
        } to R.drawable.boost
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Image(
                painter = painterResource(id = R.drawable.android),
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
}

@Preview(showBackground = true)
@Composable
fun ProfileDetailScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
                .align(Alignment.TopCenter),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Image(
                    painter = painterResource(id = R.drawable.android),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.7f),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // First icon + text
                    Icon(
                        painter = painterResource(id = R.drawable.android), // Replace
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("First Text") },
                        modifier = Modifier.weight(1f)
                    )


                    Icon(
                        painter = painterResource(id = R.drawable.android), // Replace
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Second Text") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }


        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-40).dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(5) {
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.android),
                        contentDescription = "Action Icon ${it + 1}",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

