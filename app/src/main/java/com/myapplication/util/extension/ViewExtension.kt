package com.myapplication.util.extension

import android.app.Activity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    view?.let {
        val methodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        methodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}

fun View.visible() {
    this.isVisible = true
}

fun View.gone() {
    this.isVisible = false
}
