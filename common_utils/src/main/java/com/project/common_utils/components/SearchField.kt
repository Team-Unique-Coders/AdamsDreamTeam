package com.project.common_utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Search...",
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(12.dp), clip = false)
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun SearchFieldPreview() {
    var text by remember { mutableStateOf("") }
//        CircularImageHolder(imageUrl = "", description = "What you want to keep")
    SearchField(
        value = text,
        onValueChange = { text = it },
        placeholder = "Search here..."
    )
}