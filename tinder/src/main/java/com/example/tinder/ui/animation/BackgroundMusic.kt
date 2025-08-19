package com.example.tinder.ui.animation

import com.example.tinder.R
import android.media.MediaPlayer
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun LoopingMusicButton() {
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    LaunchedEffect(Unit) {
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.romantic2).apply {
                isLooping = true
                start()
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
}
