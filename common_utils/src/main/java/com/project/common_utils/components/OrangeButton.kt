package com.project.common_utils

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview(showBackground = true)
@Composable
fun OrangeButtonPreview() {
        OrangeButton(onClick = getOrangeClickAction(),"Hello")
}

fun getOrangeClickAction(): () -> Unit {
    return {
        val data = 50
        println("Orange data: $data")
    }
}


@Composable
fun OrangeButton(onClick: () -> Unit, text:String) {

    Button(
        onClick = onClick,
        modifier = Modifier
                .width(220.dp) // Bigger width
            .height(60.dp) // Bigger height
            .shadow(8.dp, shape = RoundedCornerShape(6.dp)), // Shadow + curve
        shape = RoundedCornerShape(6.dp), // Curved rectangle
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF9800), // Orange background
            contentColor = Color.White // Text color
        )
    ) {
        Text(text)
    }
}