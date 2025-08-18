package com.example.tinder.ui.mainscreen

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tinder.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.toMutableStateList
import com.example.tinder.data.DummyDataSource
import com.example.tinder.data.DummyUserFull
import com.example.tinder.ui.AnimatedInfoCard
import com.example.tinder.ui.InfoCardContent
import com.example.tinder.ui.RainEffectController
import com.example.tinder.ui.RainEffectOverlay
import kotlinx.coroutines.flow.map



@Composable
fun ProfileDetailScreen(
    controller: RainEffectController,
    navController: NavController,
    onNext: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // Use mutableStateList so removing users triggers recomposition
    val allUsers = remember { DummyDataSource.dummyUsersFull.toMutableStateList() }
    var currentIndex by remember { mutableStateOf(0) }
    val offsetX = remember { Animatable(0f) }
    var selectedContent by remember { mutableStateOf<InfoCardContent?>(null) }

    val clickedPerProfile = remember { mutableStateMapOf<Int, MutableSet<Int>>() }
    var lastBoostClickTime by remember { mutableStateOf(0L) }

    val iconToContent = mapOf(
        R.drawable.happyadam to InfoCardContent(
            background = R.drawable.likep,
            title = "You Liked",
            description = "If it's mutual, you’ll be able to start chatting.",
            onOk = { navController.navigate("likeScreen") }
        ),
        R.drawable.love to InfoCardContent(
            background = R.drawable.srlk,
            title = "Super Like!",
            description = "Show them you’re really interested with a Super Like.",
            onOk = { navController.navigate("superLikeScreen") }
        ),
        R.drawable.adamboost to InfoCardContent(
            background = R.drawable.boostspic,
            title = "Boost your Profile",
            description = "Be the top profile in your area for 30 mins to get more matches.",
            onOk = { navController.navigate("boostScreen") }
        )
    )

    if (allUsers.isEmpty() || currentIndex >= allUsers.size) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No more profiles available")
        }
        return
    }

    val currentUser = allUsers.getOrNull(currentIndex)
    val nextUser = allUsers.getOrNull(currentIndex + 1)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // Next card slightly scaled
        nextUser?.let {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f)
                    .graphicsLayer { scaleX = 0.95f; scaleY = 0.95f },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                ProfileCardContent(user = it) {
                    navController.navigate("profileFull/${it.photo}/${it.name}/${it.age}/${it.city}")
                }
            }
        }

        // Current card
        currentUser?.let { user ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f)
                    .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                scope.launch { offsetX.snapTo(offsetX.value + dragAmount.x) }
                            },
                            onDragEnd = {
                                scope.launch {
                                    val threshold = screenWidth.value / 4
                                    if (offsetX.value > threshold || offsetX.value < -threshold) {
                                        currentIndex++
                                    }
                                    offsetX.animateTo(0f)
                                }
                            }
                        )
                    },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                ProfileCardContent(user = user) {
                    navController.navigate("profileFull/${user.photo}/${user.name}/${user.age}/${user.city}")
                }
            }
        }

        // Bottom icons
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-40).dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val icons = listOf(
                R.drawable.returnemote,
                R.drawable.sadadam,
                R.drawable.happyadam,
                R.drawable.love,
                R.drawable.adamboost
            )

            icons.forEach { iconRes ->
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            currentUser?.let { user ->
                                val clickedIcons = clickedPerProfile.getOrPut(user.id) { mutableSetOf() }

                                when (iconRes) {
                                    R.drawable.returnemote -> {
                                        onNext()
                                    }
                                    R.drawable.sadadam -> {
                                        // Remove user from the list
                                        allUsers.removeAt(currentIndex)
                                        if (currentIndex >= allUsers.size) {
                                            currentIndex = allUsers.size - 1
                                        }
                                    }
                                    R.drawable.happyadam -> {
                                        if (user.likes <= 0 || clickedIcons.contains(iconRes)) {
                                            selectedContent = iconToContent[iconRes]
                                            return@clickable
                                        }
                                        clickedIcons.add(iconRes)
                                        controller.startRainWithIcon(iconRes)
                                        selectedContent = iconToContent[iconRes]
                                    }
                                    R.drawable.love -> {
                                        if (user.superLikes <= 0 || clickedIcons.contains(iconRes)) {
                                            selectedContent = iconToContent[iconRes]
                                            return@clickable
                                        }
                                        clickedIcons.add(iconRes)
                                        controller.startRainWithIcon(iconRes)
                                        selectedContent = iconToContent[iconRes]
                                    }
                                    R.drawable.adamboost -> {
                                        val now = System.currentTimeMillis()
                                        if (user.boosts <= 0 || clickedIcons.contains(iconRes) || now - lastBoostClickTime < 30 * 60 * 1000) {
                                            selectedContent = iconToContent[iconRes]
                                            return@clickable
                                        }
                                        clickedIcons.add(iconRes)
                                        lastBoostClickTime = now
                                        controller.startRainWithIcon(iconRes)
                                        selectedContent = iconToContent[iconRes]
                                    }
                                }
                            }
                        }
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        }

        selectedContent?.let { content ->
            AnimatedInfoCard(
                show = true,
                content = content,
                onDismiss = { selectedContent = null }
            )
        }
    }

    RainEffectOverlay(controller = controller)
}








@Composable
fun ProfileCardContent(user: DummyUserFull, onClick: () -> Unit) {
    Column {
        Image(
            painter = painterResource(id = user.photo),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(0.7f)
                .fillMaxWidth()
                .clickable { onClick() }
        )
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(text = user.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${user.age} • ${user.city}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = user.aboutMe, style = MaterialTheme.typography.bodySmall)
        }
    }
}

