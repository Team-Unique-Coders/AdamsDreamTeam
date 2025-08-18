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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import coil.compose.rememberAsyncImagePainter
import com.example.tinder.R
import com.example.tinder.data.DummyDataSource
import com.example.tinder.data.DummyUserFull
import java.util.Calendar


@Composable
fun CompleteProfile(
    onGeolocation: () -> Unit,
    onProfileComplete: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var topText by remember { mutableStateOf("") }        // Name / Description
    var middleText by remember { mutableStateOf("") }     // City
    var bottomText by remember { mutableStateOf("") }     // Date of birth, format YYYYMMDD

    val selectedImages = remember { mutableStateListOf<Uri?>(null, null, null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val index = selectedImages.indexOfFirst { it == null }
        if (index != -1) selectedImages[index] = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Text fields
        OutlinedTextField(
            value = topText,
            onValueChange = { topText = it },
            label = { Text("Name / About Me") },
            modifier = Modifier.fillMaxWidth().weight(0.2f),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = middleText,
            onValueChange = { middleText = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth().weight(0.1f),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = bottomText,
            onValueChange = { bottomText = it },
            label = { Text("Date of Birth (YYYYMMDD)") },
            modifier = Modifier.fillMaxWidth(0.5f).weight(0.1f),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Photo upload
        val photoUri = selectedImages.firstOrNull()

        Card(
            modifier = Modifier
                .width(80.dp)
                .height(150.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { launcher.launch("image/*") },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            if (photoUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(photoUri),
                    contentDescription = "Selected Photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", fontSize = 24.sp, color = Color.White)
                }
            }
        }



        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (topText.isBlank() || middleText.isBlank() || bottomText.isBlank()) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                // Parse date of birth
                val dobInt = bottomText.toIntOrNull()
                val age = if (dobInt != null) calculateAge(dobInt) else 25

                val photoRes = selectedImages.firstOrNull()?.let { R.drawable.adam } ?: R.drawable.adam // placeholder

                val newUser = DummyUserFull(
                    id = 1,
                    name = topText,
                    age = age,
                    city = middleText,
                    photo = photoRes,
                    aboutMe = topText,
                    description = "Just a funny description for $topText",
                    dateOfBirth = dobInt ?: 20000101,
                    createdTime = System.currentTimeMillis().toString(),
                    boosts = 0,
                    likes = 0,
                    superLikes = 0
                )

                val index = DummyDataSource.dummyUsersFull.indexOfFirst { it.id == 1 }
                if (index != -1) {
                    DummyDataSource.dummyUsersFull[index] = newUser
                } else {
                    DummyDataSource.dummyUsersFull.add(newUser)
                }

                onGeolocation()
                onProfileComplete()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Next", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

fun calculateAge(dob: Int): Int {
    val dobStr = dob.toString()
    if (dobStr.length != 8) return 0
    val year = dobStr.substring(0, 4).toInt()
    val month = dobStr.substring(4, 6).toInt() - 1
    val day = dobStr.substring(6, 8).toInt()

    val dobCalendar = Calendar.getInstance().apply { set(year, month, day) }
    val today = Calendar.getInstance()
    var age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)
    if (today.get(Calendar.MONTH) < dobCalendar.get(Calendar.MONTH) ||
        (today.get(Calendar.MONTH) == dobCalendar.get(Calendar.MONTH) &&
                today.get(Calendar.DAY_OF_MONTH) < dobCalendar.get(Calendar.DAY_OF_MONTH))
    ) {
        age--
    }
    return age
}






