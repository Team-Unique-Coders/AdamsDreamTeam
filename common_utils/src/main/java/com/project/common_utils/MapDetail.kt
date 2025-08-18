package com.project.common_utils

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
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.common_utils.components.CircularImageHolderDrawable
import com.project.common_utils.components.OrangeButton

@Composable
fun MapDetailComponent(
    name: String = "Jenny Jones",
    image: Int = R.drawable.profiled,
    imageDesc: String = "An image of ${name}",
    stats: TextSpacerTextModel = TextSpacerTextModel("⭐ 4.8", "$15/kg"),
    location: List<IconTextModel> = listOf(
        IconTextModel(
            "🌎",
            listOf("28 Broad Street", "Singapore")
        )
    ),
    iconTextList: List<IconTextModel> = listOf(),
    textSpacerTextList: List<TextSpacerTextModel> = listOf(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding(),
    ) {
        // Content that takes up a specific portion of the screen
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box {
                MainContent(name, image, imageDesc, stats, location, iconTextList, textSpacerTextList)
            }
        }
    }
}

@Composable
fun MainContent(
    name: String,
    image: Int,
    imageDesc: String,
    stats: TextSpacerTextModel,
    location: List<IconTextModel>,
    iconTextList: List<IconTextModel>,
    textSpacerTextList: List<TextSpacerTextModel>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
            Text(name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            TextSpacerText(stats)
            Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ", modifier = Modifier.padding(top = 24.dp))
            HorizontalDivider(
                thickness = 1.dp, // Adjust the thickness of the line
                color = Color.LightGray, // Set the color of the line
                modifier = Modifier.padding(24.dp)
            )
            IconTextList(location)
            IconTextList(iconTextList)
            TextSpacerTextList(textSpacerTextList)
            OrangeButton({}, "Take appointment")
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = 0.dp, y = (-50).dp),
        contentAlignment = Alignment.Center
    ) {
        CircularImageHolderDrawable(image, imageDesc)
    }
}

@Composable
fun TextSpacerTextList(textSpaceTextList: List<TextSpacerTextModel>) {
    for (textSpacerTextModel in textSpaceTextList) {
        TextSpacerText(textSpacerTextModel)

        HorizontalDivider(
            thickness = 1.dp, // Adjust the thickness of the line
            color = Color.LightGray, // Set the color of the line
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
fun TextSpacerText(textSpacerTextModel: TextSpacerTextModel) {
    Row {
        Text(textSpacerTextModel.service, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))
        Text(textSpacerTextModel.cost, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun IconTextList(iconTextList: List<IconTextModel>) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column() {
            for (iconTextModel in iconTextList) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconText(iconTextModel)
                }

                HorizontalDivider(
                    thickness = 1.dp, // Adjust the thickness of the line
                    color = Color.LightGray, // Set the color of the line
                    modifier = Modifier.padding(24.dp)
                )
            }
        }
    }
}

@Composable
fun IconText(iconTextModel: IconTextModel) {
    Text(iconTextModel.icon, modifier = Modifier.padding(end = 20.dp))
    Column() {
        for (text in iconTextModel.textList) {
            Text(text)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapDetailPreview() {
    MapDetailComponent()
}