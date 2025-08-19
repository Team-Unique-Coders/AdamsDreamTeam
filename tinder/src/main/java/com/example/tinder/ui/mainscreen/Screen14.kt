package com.example.tinder.ui.mainscreen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.tinder.R
import com.example.tinder.data.DummyAdamUser
import androidx.core.net.toUri
import com.example.tinder.data.PhotoSource


@Composable
fun ModifyProfile(onNext: () -> Unit) {
    val context = LocalContext.current
    val adam = DummyAdamUser.dummyUsersFull.firstOrNull { it.id == 1 }

    var topText by remember { mutableStateOf(adam?.aboutMe ?: "") }
    var middleText by remember { mutableStateOf(adam?.city ?: "") }
    var bottomText by remember { mutableStateOf(adam?.dateOfBirth?.toString() ?: "") }
    //Really Important ~ saves images ~
    var photoUris by remember {
        mutableStateOf<List<Uri>>(
            adam?.photoUris?.mapNotNull {
                (it as? PhotoSource.UriString)?.uri?.toUri()
            } ?: emptyList()
        )
    }


    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        if (!uris.isNullOrEmpty()) {
            photoUris = uris
        }
    }

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
            modifier = Modifier.fillMaxWidth().height(100.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = middleText,
            onValueChange = { middleText = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth().height(70.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = bottomText,
            onValueChange = { bottomText = it },
            label = { Text("Date of Birth (YYYYMMDD)") },
            modifier = Modifier.fillMaxWidth(0.7f).height(70.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(24.dp))

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
            (0 until 3).forEach { index ->
                Card(
                    modifier = Modifier
                        .width(110.dp)
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { launcher.launch("image/*") },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    if (photoUris.getOrNull(index) != null) {
                        Image(
                            painter = rememberAsyncImagePainter(photoUris[index]),
                            contentDescription = "Photo ${index + 1}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", fontSize = 28.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val updatedUser = adam?.copy(
                    aboutMe = topText,
                    city = middleText,
                    dateOfBirth = bottomText.toIntOrNull() ?: 0,
                    photoUris = photoUris.map { PhotoSource.UriString(it.toString()) }
                )
                if (updatedUser != null) {
                    DummyAdamUser.saveOrUpdateUser(updatedUser)
                    Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                }
                onNext()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Update",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

