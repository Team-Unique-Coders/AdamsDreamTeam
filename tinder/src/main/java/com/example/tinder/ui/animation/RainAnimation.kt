package com.example.tinder.ui.animation

import android.animation.ObjectAnimator
import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import kotlin.random.Random

//animation test


@Composable
fun RainEffectOverlay(controller: RainEffectController) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            FrameLayout(context).apply {
                controller.bind(this, context)
            }
        },
        onRelease = {
            controller.unbind()
        }
    )
}

fun createFallingIcon(resId: Int, rootLayout: ViewGroup, context: Context) {
    val imageView = ImageView(context)
    imageView.setImageResource(resId)

    val size = Random.nextInt(100, 200)
    val params = ViewGroup.LayoutParams(size, size)
    imageView.layoutParams = params

    val screenWidth = context.resources.displayMetrics.widthPixels
    imageView.x = Random.nextInt(0, screenWidth - size).toFloat()
    imageView.y = -size.toFloat()

    rootLayout.addView(imageView)

    val screenHeight = context.resources.displayMetrics.heightPixels
    val animator = ObjectAnimator.ofFloat(imageView, "translationY", screenHeight.toFloat())
    animator.duration = Random.nextLong(3000, 6000)
    animator.addUpdateListener {
        if (imageView.y > screenHeight) {
            rootLayout.removeView(imageView)
        }
    }
    animator.start()
}