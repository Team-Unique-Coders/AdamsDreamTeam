package com.example.laundry.data

//package com.project.common_utils
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.safeContentPadding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.project.common_utils.components.CircularImageHolderDrawable
//import com.project.common_utils.components.OrangeButton
//
//@Composable
//fun MapDetailComponent(
//    name: String = "Jenny Jones",
//    image: Int = R.drawable.profiled,
//    imageDesc: String = "An image of ${name}",
//    stats: TextSpacerTextModel = TextSpacerTextModel("⭐ 4.8", "$15/kg"),
//    location: List<IconTextModel> = listOf(
//        IconTextModel(
//            "🌎",
//            listOf("28 Broad Street", "Singapore")
//        )
//    ),
//    iconTextList: List<IconTextModel> = listOf(),
//    textSpacerTextList: List<TextSpacerTextModel> = listOf(),
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .safeContentPadding(),
//    ) {
//        // Content that takes up a specific portion of the screen
//        Column(
//            modifier = Modifier
//                .fillMaxSize(),
//        ) {
//            Spacer(modifier = Modifier.weight(1f))
//            Box {
//                MainContent(name, image, imageDesc, stats, location, iconTextList, textSpacerTextList)
//            }
//        }
//    }
//}
//
//@Composable
//fun MainContent(
//    name: String,
//    image: Int,
//    imageDesc: String,
//    stats: TextSpacerTextModel,
//    location: List<IconTextModel>,
//    iconTextList: List<IconTextModel>,
//    textSpacerTextList: List<TextSpacerTextModel>
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(color = Color.White),
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp)
//            )
//            Text(name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
//            TextSpacerText(stats)
//            Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ", modifier = Modifier.padding(top = 24.dp))
//            HorizontalDivider(
//                thickness = 1.dp, // Adjust the thickness of the line
//                color = Color.LightGray, // Set the color of the line
//                modifier = Modifier.padding(24.dp)
//            )
//            IconTextList(location)
//            IconTextList(iconTextList)
//            TextSpacerTextList(textSpacerTextList)
//            OrangeButton({}, "Take appointment")
//        }
//    }
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .offset(x = 0.dp, y = (-50).dp),
//        contentAlignment = Alignment.Center
//    ) {
//        CircularImageHolderDrawable(image, imageDesc)
//    }
//}
//
//@Composable
//fun TextSpacerTextList(textSpaceTextList: List<TextSpacerTextModel>) {
//    for (textSpacerTextModel in textSpaceTextList) {
//        TextSpacerText(textSpacerTextModel)
//
//        HorizontalDivider(
//            thickness = 1.dp, // Adjust the thickness of the line
//            color = Color.LightGray, // Set the color of the line
//            modifier = Modifier.padding(24.dp)
//        )
//    }
//}
//
//@Composable
//fun TextSpacerText(textSpacerTextModel: TextSpacerTextModel) {
//    Row {
//        Text(textSpacerTextModel.service, fontWeight = FontWeight.Bold)
//        Spacer(modifier = Modifier.weight(1f))
//        Text(textSpacerTextModel.cost, fontWeight = FontWeight.Bold)
//    }
//}
//
//@Composable
//fun IconTextList(iconTextList: List<IconTextModel>) {
//    Box(modifier = Modifier.fillMaxWidth()) {
//        Column() {
//            for (iconTextModel in iconTextList) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(0.6f),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    IconText(iconTextModel)
//                }
//
//                HorizontalDivider(
//                    thickness = 1.dp, // Adjust the thickness of the line
//                    color = Color.LightGray, // Set the color of the line
//                    modifier = Modifier.padding(24.dp)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun IconText(iconTextModel: IconTextModel) {
//    Text(iconTextModel.icon, modifier = Modifier.padding(end = 20.dp))
//    Column() {
//        for (text in iconTextModel.textList) {
//            Text(text)
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MapDetailPreview() {
//    MapDetailComponent()
//}
//
//package com.example.laundry.data
//
//data class Provider(
//    val name: String,
//    val rating: Double,
//    val distanceMiles: Double,
//    val priceText: String,
//    val photoUrl: String,
//    val lat: Double,
//    val lon: Double,
//    val address: String,
//    val services: List<Pair<String, String>>,
//    val about: String = "Friendly professional with fast turnaround and great quality."
//)
//
///** Optional stable key if you need an ID for navigation */
//fun providerKey(p: Provider): String =
//    "%s|%.5f|%.5f".format(java.util.Locale.US, p.name, p.lat, p.lon)
//
//val fake: List<Provider> = listOf(
//    Provider(
//        "Jenny Jones", 4.8, 4.5, "$ 15/kg",
//        "https://images.pexels.com/photos/532220/pexels-photo-532220.jpeg",
//        -6.396, 106.823, "28 Broad Street, Johannesburg",
//        listOf("Cleaning" to "$15/kg", "Dry cleaning" to "$10", "Ironing" to "$3/kg")
//    ),
//    Provider(
//        "Sacha Down", 4.7, 3.9, "$ 13/kg",
//        "https://images.pexels.com/photos/3765120/pexels-photo-3765120.jpeg",
//        -6.394, 106.820, "15 Orchard Rd, Johannesburg",
//        listOf("Cleaning" to "$13/kg", "Dry cleaning" to "$12", "Ironing" to "$3/kg")
//    ),
//    Provider(
//        "Clean & Care Co.", 4.6, 2.2, "$ 12/kg",
//        "https://images.pexels.com/photos/3735612/pexels-photo-3735612.jpeg",
//        -6.398, 106.826, "12 Victoria Ave, Johannesburg",
//        listOf("Cleaning" to "$12/kg", "Dry cleaning" to "$11", "Ironing" to "$2/kg")
//    ),
//    Provider(
//        "Sparkle Laundry", 4.9, 1.8, "$ 16/kg",
//        "https://images.pexels.com/photos/3768146/pexels-photo-3768146.jpeg",
//        -6.400, 106.824, "9 Kingsway, Johannesburg",
//        listOf("Cleaning" to "$16/kg", "Dry cleaning" to "$13", "Ironing" to "$4/kg")
//    ),
//    Provider(
//        "Pressed & Fresh", 4.5, 5.1, "$ 11/kg",
//        "https://images.pexels.com/photos/4621479/pexels-photo-4621479.jpeg",
//        -6.392, 106.818, "34 Melrose Rd, Johannesburg",
//        listOf("Cleaning" to "$11/kg", "Dry cleaning" to "$9", "Ironing" to "$2/kg")
//    ),
//    Provider(
//        "EcoWash Hub", 4.4, 6.4, "$ 10/kg",
//        "https://images.pexels.com/photos/9797308/pexels-photo-9797308.jpeg",
//        -6.389, 106.830, "5 Oak Street, Johannesburg",
//        listOf("Cleaning" to "$10/kg", "Dry cleaning" to "$9", "Ironing" to "$2/kg"),
//        about = "Eco-friendly detergents and water-saving machines."
//    ),
//    Provider(
//        "City Quick Clean", 4.3, 3.2, "$ 14/kg",
//        "https://images.pexels.com/photos/3965545/pexels-photo-3965545.jpeg",
//        -6.401, 106.829, "88 Market Lane, Johannesburg",
//        listOf("Cleaning" to "$14/kg", "Dry cleaning" to "$12", "Ironing" to "$3/kg")
//    ),
//    Provider(
//        "Laundry Lounge", 4.8, 2.7, "$ 15/kg",
//        "https://images.pexels.com/photos/3865713/pexels-photo-3865713.jpeg",
//        -6.395, 106.832, "21 Baker Street, Johannesburg",
//        listOf("Cleaning" to "$15/kg", "Dry cleaning" to "$11", "Ironing" to "$3/kg"),
//        about = "Coffee, Wi-Fi, and comfy seats while you wait."
//    ),
//    Provider(
//        "Bright Whites", 4.2, 7.0, "$ 9/kg",
//        "https://images.pexels.com/photos/8952536/pexels-photo-8952536.jpeg",
//        -6.387, 106.817, "3 Cedar Park, Johannesburg",
//        listOf("Cleaning" to "$9/kg", "Dry cleaning" to "$8", "Ironing" to "$2/kg")
//    ),
//    Provider(
//        "Golden Hangers", 4.6, 4.0, "$ 13/kg",
//        "https://images.pexels.com/photos/3769747/pexels-photo-3769747.jpeg",
//        -6.393, 106.828, "19 Newlands Ave, Johannesburg",
//        listOf("Cleaning" to "$13/kg", "Dry cleaning" to "$12", "Ironing" to "$3/kg")
//    )
//)