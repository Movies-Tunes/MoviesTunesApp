package com.myapplication.util.bindingadapter

import android.view.View
import androidx.databinding.BindingAdapter
import com.myapplication.util.extension.gone
import com.myapplication.util.extension.visible

@BindingAdapter("visisibility_view")
fun bindViewVisibility(view: View, isVisible: Boolean) {
    if (isVisible) view.visible() else view.gone()
}
