package com.example.laundry.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.laundry.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.unit.sp
import com.example.laundry.navigation.LaundryDestinations
import java.nio.file.WatchEvent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaundryHomeScreen(
    onOpen: (String) -> Unit = {},
    onBack: () -> Unit = {},
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Filled.Home, contentDescription = "Go to Home")
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            item {
                // Container Box to stack image, white Box, and orange box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp) // total height for overlapping
                ) {
                    // Top Image
                    Image(
                        painter = painterResource(R.drawable.maskgroup),
                        contentDescription = "background",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp), // adjust height as needed
                        contentScale = ContentScale.Crop
                    )

                    // Orange Box behind White Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .align(Alignment.BottomCenter) // touches bottom of container
                            .background(Color(0xFFFF9800))
                    ){
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically) {
                            IconTextButton(com.project.common_utils.R.drawable.heart,"Favorites") { }
                            IconTextButton(R.drawable.order,"Orders") { }
                        }
                    }

                    // Main White Box floating above
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .align(Alignment.Center) // center over container
                            .padding(horizontal = 20.dp)
                            .offset(y = (30).dp)
                            .shadow(
                                elevation = 16.dp,
                                shape = RoundedCornerShape(16.dp),
                                clip = false
                            )
                            .background(Color.White, shape = RoundedCornerShape(16.dp))
                    ) {
                        WhiteBoxValue()
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Row {
                        Text("House Cleaner", color = Color.Black)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("120", color = Color.LightGray)
                    }
                    IconButton(onClick = {
                        onOpen(LaundryDestinations.FILTER)
                    }) {
                        Icon(
                            painter = painterResource(id = com.project.common_utils.R.drawable.options),
                            contentDescription = "Options",
                            modifier = Modifier.size(24.dp),
                            tint = Color(0xFFFF9800)
                        )
                    }
                }
            }

            // Additional list items below
            items(10) { index ->
                Box(
                    modifier = Modifier.padding(0.dp,0.dp,0.dp,20.dp),

                ){
                    CardDetails(index,onOpen)
                }
            }
            items(10) { index ->
                Spacer(Modifier.height(3.dp))
            }
        }
    }
}




@Composable
fun IconTextButton(
    iconId: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }  // Entire Row is clickable
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .background(
                color = Color.Transparent, // optional background
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp), // inner padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = text,
            modifier = Modifier.size(24.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun WhiteBoxValue(){
    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Johannesburg, 1 Road Ubuntu",
                fontSize = 15.sp)
            Icon(
                painter = painterResource(id = R.drawable.location),
                contentDescription = "Options",
                modifier = Modifier.size(24.dp),
                tint = Color(0xFFFF9800)
            )
        }
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        Row(
            modifier =Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            WhiteBoxValueColumn("CHOOSE DATES","20 Mar - 10h")
            VerticalDivider( modifier = Modifier
                .height(52.dp), DividerDefaults.Thickness, DividerDefaults.color)
            WhiteBoxValueColumn("KG","5")
            VerticalDivider(modifier = Modifier
                .height(52.dp), DividerDefaults.Thickness, DividerDefaults.color)
            WhiteBoxValueColumn("DRY","2")
            VerticalDivider(modifier = Modifier
                .height(52.dp), DividerDefaults.Thickness, DividerDefaults.color)
            WhiteBoxValueColumn("IRONING","Yes")
        }
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        Column(
            modifier = Modifier.padding(0.dp,10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp,0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Search location/Name")
                Icon(
                    painter = painterResource(com.project.common_utils.R.drawable.search),
                    contentDescription = "searchIcon"
                )
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800), // Orange background
                    contentColor = Color.White // Text color
                )
                ) {
                Text("Search",
                    modifier = Modifier.padding(70.dp,0.dp)
                )
            }
        }

    }
}

@Composable
fun WhiteBoxValueColumn(heading:String,value:String){
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
            Text(heading,
                color = Color.LightGray,
                fontSize = 13.sp
            )
            Text(value,
                color = Color.Black,
                fontSize = 15.sp
            )
    }
}

@Composable
fun CardDetails(
    index:Int,
    onOpen: (String) -> Unit = {}
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            // center over container
            .padding(horizontal = 20.dp)
            .offset(y = (30).dp)
            .clickable{ onOpen(LaundryDestinations.MAPSCREEN)}
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false
            )
            .background(Color.White, shape = RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.card),
                contentDescription = "cardImage",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                Text(
                    "Jessy Jones",
                    color = Color.Black,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    "Johannesburg",
                    color = Color.LightGray,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            }

            Row (
                modifier = Modifier.fillMaxWidth().padding(10.dp,0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                CardDetailsValues(com.project.common_utils.R.drawable.stars,"4.8")
                CardDetailsValues(R.drawable.route,"500 m")
                Text("$ 15/kg",fontSize = 12.sp)
            }

        }
    }
}

@Composable
fun CardDetailsValues(id: Int, value: String){
    Row {
        Icon(
            painter = painterResource(id),
            contentDescription = "star",
            modifier =Modifier.size(14.dp),
            tint = Color(0xFFFF9800)
        )
        Text(
            value,
            fontSize = 12.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LaundryHomeScreenPreview(){
    LaundryHomeScreen()
}
