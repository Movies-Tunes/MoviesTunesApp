package com.myapplication.util.extension

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.myapplication.R

fun Fragment.snackbar(
    view: View = requireView(),
    message: String,
    length: Int = Snackbar.LENGTH_SHORT,
) {
    val snackbar = Snackbar.make(view, message, length)
    snackbar.setAction(getString(R.string.message_dismiss_snackbar)) {
        snackbar.dismiss()
    }
    snackbar.show()
}
fun Fragment.toast(
    context: Context = requireContext(),
    message: String,
    length: Int = Toast.LENGTH_SHORT,
) {
    Toast.makeText(context, message, length).show()
}

fun Fragment.createLoadingDialog(): Dialog {
    val loadingDialog = Dialog(requireContext())
    loadingDialog.setContentView(R.layout.progress_dialog)
    return loadingDialog
}
