package com.example.tinder.ui.mainscreen

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.tinder.data.DummyAdamUser
import com.example.tinder.data.DummyDataSource
import com.example.tinder.data.DummyUserFull
import com.example.tinder.data.PhotoSource
import com.example.tinder.nav.navigateToProfileFull
import com.example.tinder.ui.animation.AnimatedInfoCard
import com.example.tinder.ui.animation.InfoCardContent
import com.example.tinder.ui.animation.RainEffectController
import com.example.tinder.ui.animation.RainEffectOverlay
import kotlinx.coroutines.delay
import kotlin.math.log


@Composable
fun ProfileDetailScreen(
    controller: RainEffectController,
    navController: NavController,
    onNext: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val allUsers = remember { DummyDataSource.dummyUsersFull.toMutableStateList() }
    var currentIndex by remember { mutableStateOf(0) }
    val offsetX = remember { Animatable(0f) }
    var selectedContent by remember { mutableStateOf<InfoCardContent?>(null) }
    val clickedPerProfile = remember { mutableStateMapOf<Int, MutableSet<Int>>() }
    var lastBoostClickTime by remember { mutableStateOf(0L) }
    var activeGlow by remember { mutableStateOf<GlowType?>(null) }

    val glowColor by animateColorAsState(
        targetValue = when (activeGlow) {
            GlowType.BOOST -> Color(0xFFFF9800)
            GlowType.LIKE -> Color(0xFF4CAF50)
            GlowType.SUPERLIKE -> Color(0xFFFFEB3B)
            else -> Color.Transparent
        },
        label = "GlowAnim"
    )

    val iconToContent = mapOf(
        R.drawable.like to InfoCardContent(
            background = R.drawable.likep,
            title = "You Liked",
            description = "If it's mutual, you’ll be able to start chatting.",
            onOk = { navController.navigate("likeScreen") }
        ),
        R.drawable.superlik to InfoCardContent(
            background = R.drawable.srlk,
            title = "Super Like!",
            description = "Show them you’re really interested with a Super Like.",
            onOk = { navController.navigate("superLikeScreen") }
        ),
        R.drawable.boost to InfoCardContent(
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
        nextUser?.let { user ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f)
                    .graphicsLayer { scaleX = 0.95f; scaleY = 0.95f },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                ProfileCardContent(user = user) {
                    val firstPhoto = user.photoUris.firstOrNull()
                    if (firstPhoto != null) {
                        when (firstPhoto) {
                            is PhotoSource.DrawableRes -> {
                                navController.navigateToProfileFull(userId = user.id)
                            }
                            is PhotoSource.UriString -> {
                                navController.navigateToProfileFull(userId = user.id)
                            }
                        }
                    }
                }
            }
        }

        currentUser?.let { user ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f)
                    .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                    .border(
                        width = if (activeGlow != null) 4.dp else 0.dp,
                        color = glowColor,
                        shape = RoundedCornerShape(16.dp)
                    )
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
                    val firstPhoto = user.photoUris.firstOrNull()
                    if (firstPhoto != null) {
                        when (firstPhoto) {
                            is PhotoSource.DrawableRes -> {
                                navController.navigateToProfileFull(userId = user.id)
                            }
                            is PhotoSource.UriString -> {
                                navController.navigateToProfileFull(userId = user.id)
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-40).dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val icons = listOf(
                R.drawable.back,
                R.drawable.block,
                R.drawable.like,
                R.drawable.superlik,
                R.drawable.boost
            )

            icons.forEach { iconRes ->
                val isGlowing = when {
                    activeGlow == GlowType.BOOST && iconRes == R.drawable.boost -> true
                    activeGlow == GlowType.LIKE && iconRes == R.drawable.like -> true
                    activeGlow == GlowType.SUPERLIKE && iconRes == R.drawable.superlik -> true
                    else -> false
                }

                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .size(50.dp)
                        .border(
                            width = if (isGlowing) 3.dp else 0.dp,
                            color = glowColor,
                            shape = CircleShape
                        )
                        .clickable {
                            val adam = DummyAdamUser.dummyUsersFull.firstOrNull { it.id == 1 }
                            val clickedIcons = clickedPerProfile.getOrPut(currentUser?.id ?: -1) { mutableSetOf() }

                            when (iconRes) {
                                R.drawable.back -> onNext()
                                R.drawable.block -> {
                                    allUsers.removeAt(currentIndex)
                                    if (currentIndex >= allUsers.size) {
                                        currentIndex = allUsers.size - 1
                                    }
                                }
                                R.drawable.like -> {
                                    adam?.let {
                                        if (it.likes < 1) {
                                            selectedContent = iconToContent[iconRes]
                                            return@clickable
                                        }
                                        DummyAdamUser.updateUserStat("likes", -1)
                                        clickedIcons.add(iconRes)
                                        controller.startRainWithIcon(iconRes)
                                        scope.launch {
                                            activeGlow = GlowType.LIKE
                                            delay(800)
                                            activeGlow = null
                                        }
                                        selectedContent = null
                                    }
                                }
                                R.drawable.superlik -> {
                                    adam?.let {
                                        if (it.superLikes < 1) {
                                            selectedContent = iconToContent[iconRes]
                                            return@clickable
                                        }
                                        DummyAdamUser.updateUserStat("superLikes", -1)
                                        clickedIcons.add(iconRes)
                                        controller.startRainWithIcon(iconRes)
                                        scope.launch {
                                            activeGlow = GlowType.SUPERLIKE
                                            delay(800)
                                            activeGlow = null
                                        }
                                        selectedContent = null
                                    }
                                }
                                R.drawable.boost -> {
                                    val now = System.currentTimeMillis()
                                    adam?.let {
                                        if (it.boosts < 1 || clickedIcons.contains(iconRes) || now - lastBoostClickTime < 30 * 60 * 1000) {
                                            selectedContent = iconToContent[iconRes]
                                            return@clickable
                                        }
                                        DummyAdamUser.updateUserStat("boosts", -1)
                                        lastBoostClickTime = now
                                        clickedIcons.add(iconRes)
                                        controller.startRainWithIcon(iconRes)
                                        scope.launch {
                                            activeGlow = GlowType.BOOST
                                            delay(800)
                                            activeGlow = null
                                        }
                                        selectedContent = null
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

enum class GlowType { BOOST, LIKE, SUPERLIKE }








@Composable
fun ProfileCardContent(user: DummyUserFull, onClick: () -> Unit) {
    val first = user.photoUris.firstOrNull()
    val painter = when (first) {
        is PhotoSource.DrawableRes -> painterResource(first.resId)
        is PhotoSource.UriString -> rememberAsyncImagePainter(first.uri)
        null -> painterResource(id = R.drawable.android)
    }

    Column {
        Image(
            painter = painter,
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(0.7f)
                .fillMaxWidth()
                .clickable { onClick() }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = user.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${user.age} • ${user.city}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = user.aboutMe, style = MaterialTheme.typography.bodySmall)
        }
    }
}




