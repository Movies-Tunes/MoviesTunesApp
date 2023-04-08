package com.myapplication.util.extension

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.snackbar(
    view: View = requireView(),
    message: String,
    length: Int = Snackbar.LENGTH_SHORT,
) {
    Snackbar.make(view, message, length).show()
}
fun Fragment.toast(
    context: Context = requireContext(),
    message: String,
    length: Int = Toast.LENGTH_SHORT,
) {
    Toast.makeText(context, message, length).show()
}
