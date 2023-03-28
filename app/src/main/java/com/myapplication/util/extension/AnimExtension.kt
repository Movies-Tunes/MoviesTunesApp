package com.myapplication.util.extension

import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes

fun View.startAnimationWithId(@AnimRes id: Int, duration: Long = 2000) {
    AnimationUtils.loadAnimation(this.context, id).also { loadedAnimation ->
        loadedAnimation.duration = duration
        this.startAnimation(loadedAnimation)
    }
}
