package com.example.tinder.ui.animation

import android.content.Context
import android.view.ViewGroup
import kotlin.random.Random

class RainEffectController {
    private var rootLayout: ViewGroup? = null
    private var context: Context? = null

    fun bind(root: ViewGroup, ctx: Context) {
        rootLayout = root
        context = ctx
    }

    fun unbind() {
        rootLayout = null
        context = null
    }

    fun startRainWithIcon(iconRes: Int) {
        val layout = rootLayout ?: return
        val ctx = context ?: return
        //default this is one rain icon
        //add a repeat and specify how much you want
        val randomCount = Random.nextInt(5, 50)
        repeat(randomCount) {
            createFallingIcon(iconRes, layout, ctx)
        }
        /* to have it rain continuously
        we have to add to add a coroutine
        rainjob?.cancel()

        rainjob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
            if(layout.childCount < 10) {
                createFallingIcon(iconRes, layout, ctx)
            }
            }

            then in a separate function use
            fun stopRain() {
            rainjob?.cancel()
            rainjob = null
            }

         */
    }
}
