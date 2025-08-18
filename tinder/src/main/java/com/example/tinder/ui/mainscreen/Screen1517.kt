package com.example.tinder.ui.mainscreen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tinder.R
import com.example.tinder.data.DummyDataSource


@Composable
fun TwoImageCard(
    topImage: Int,
    bottomImage: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Image(
            painter = painterResource(id = topImage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        )

        Image(
            painter = painterResource(id = bottomImage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}




@Composable
fun BoostScreen(onNext: () -> Unit, onNoThanks: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TwoImageCard(
            topImage = R.drawable.boostspic,
            bottomImage = R.drawable.boostbottom,
            modifier = Modifier.fillMaxWidth()
        )

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    val userIndex = DummyDataSource.dummyUsersFull.indexOfFirst { it.id == 1 }
                    if (userIndex != -1) {
                        val user = DummyDataSource.dummyUsersFull[userIndex]
                        DummyDataSource.dummyUsersFull[userIndex] =
                            user.copy(boosts = user.boosts + 5)
                    }
                    onNext()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Next", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            OutlinedButton(
                onClick = onNoThanks,
                border = BorderStroke(1.dp, Color(0xFFFF9800)),
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("No, Thanks", color = Color(0xFFFF9800), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun LikeScreen(onNext: () -> Unit, onNoThanks: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TwoImageCard(
            topImage = R.drawable.likep,
            bottomImage = R.drawable.likebottom,
            modifier = Modifier.fillMaxWidth()
        )

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    val userIndex = DummyDataSource.dummyUsersFull.indexOfFirst { it.id == 1 }
                    if (userIndex != -1) {
                        val user = DummyDataSource.dummyUsersFull[userIndex]
                        DummyDataSource.dummyUsersFull[userIndex] =
                            user.copy(likes = user.likes + 5)
                    }
                    onNext()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Next", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            OutlinedButton(
                onClick = onNoThanks,
                border = BorderStroke(1.dp, Color(0xFFFF9800)),
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("No, Thanks", color = Color(0xFFFF9800), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SuperLikeScreen(onNext: () -> Unit, onNoThanks: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TwoImageCard(
            topImage = R.drawable.srlk,
            bottomImage = R.drawable.superbottom,
            modifier = Modifier.fillMaxWidth()
        )

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    val userIndex = DummyDataSource.dummyUsersFull.indexOfFirst { it.id == 1 }
                    if (userIndex != -1) {
                        val user = DummyDataSource.dummyUsersFull[userIndex]
                        DummyDataSource.dummyUsersFull[userIndex] =
                            user.copy(superLikes = user.superLikes + 5)
                    }
                    onNext()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Next", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            OutlinedButton(
                onClick = onNoThanks,
                border = BorderStroke(1.dp, Color(0xFFFF9800)),
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("No, Thanks", color = Color(0xFFFF9800), fontWeight = FontWeight.Bold)
            }
        }
    }
}



