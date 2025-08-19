// file: com/example/bank/presentation/onboarding/CreateAccountScreen.kt
package com.example.bank.presentation.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.project.common_utils.components.OrangeButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(
    onBack: () -> Unit,
    onSubmit: (fullName: String, contact: String, pin: String) -> Unit,
    @DrawableRes illustrationRes: Int = com.example.bank.R.drawable.createimg // use your existing asset
) {
    val brandOrange = Color(0xFFFF7A1A)

    var fullName by rememberSaveable { mutableStateOf("") }
    var contact  by rememberSaveable { mutableStateOf("") }
    var pin      by rememberSaveable { mutableStateOf("") }
    var pin2     by rememberSaveable { mutableStateOf("") }

    var nameErr    by rememberSaveable { mutableStateOf<String?>(null) }
    var contactErr by rememberSaveable { mutableStateOf<String?>(null) }
    var pinErr     by rememberSaveable { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        nameErr = if (fullName.isBlank()) "Please enter your name" else null
        val isEmail = contact.contains("@") && contact.contains(".")
        val isPhone = contact.filter { it.isDigit() }.length >= 7
        contactErr = if (!isEmail && !isPhone) "Enter a valid email or phone" else null
        val pinOk = pin.length in 4..6 && pin.all { it.isDigit() }
        val match = (pin == pin2)
        pinErr = when {
            !pinOk -> "PIN must be 4–6 digits"
            !match -> "PINs don’t match"
            else -> null
        }
        return nameErr == null && contactErr == null && pinErr == null
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Create account") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    navigationIconContentColor = Color.Black,
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header illustration – on-brand, occupies visual space
            Spacer(Modifier.height(4.dp))
            Image(
                painter = painterResource(illustrationRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(top = 4.dp, bottom = 8.dp)
            )

            // Form
            OutlinedTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                    if (nameErr != null) nameErr = null
                },
                label = { Text("Full name") },
                singleLine = true,
                isError = nameErr != null,
                supportingText = { if (nameErr != null) Text(nameErr!!) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = brandOrange,
                    unfocusedIndicatorColor = Color(0xFFE6E6E6),
                    cursorColor = Color.Black
                )
            )

            OutlinedTextField(
                value = contact,
                onValueChange = {
                    contact = it
                    if (contactErr != null) contactErr = null
                },
                label = { Text("Email or phone") },
                singleLine = true,
                isError = contactErr != null,
                supportingText = { if (contactErr != null) Text(contactErr!!) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = brandOrange,
                    unfocusedIndicatorColor = Color(0xFFE6E6E6),
                    cursorColor = Color.Black
                )
            )

            OutlinedTextField(
                value = pin,
                onValueChange = {
                    if (it.length <= 6) pin = it.filter { c -> c.isDigit() }
                    if (pinErr != null) pinErr = null
                },
                label = { Text("Set PIN (4–6 digits)") },
                singleLine = true,
                isError = pinErr != null,
                visualTransformation = PasswordVisualTransformation(),
                supportingText = { if (pinErr != null) Text(pinErr!!) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = brandOrange,
                    unfocusedIndicatorColor = Color(0xFFE6E6E6),
                    cursorColor = Color.Black
                )
            )

            OutlinedTextField(
                value = pin2,
                onValueChange = {
                    if (it.length <= 6) pin2 = it.filter { c -> c.isDigit() }
                    if (pinErr != null) pinErr = null
                },
                label = { Text("Confirm PIN") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = brandOrange,
                    unfocusedIndicatorColor = Color(0xFFE6E6E6),
                    cursorColor = Color.Black
                )
            )

            Spacer(Modifier.weight(1f))

            // On-brand action button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OrangeButton(
                    onClick = { if (validate()) onSubmit(fullName.trim(), contact.trim(), pin) },
                    text = "Create account"
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
