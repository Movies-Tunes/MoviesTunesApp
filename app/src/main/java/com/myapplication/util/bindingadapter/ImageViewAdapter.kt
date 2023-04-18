package com.myapplication.util.bindingadapter

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.myapplication.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

@BindingAdapter("load_image")
fun loadImage(imageView: ImageView?, url: String?) {
    url?.let {
        Picasso.get().load(url).placeholder(R.drawable.rotate_loading).into(
            imageView,
            object : Callback {
                override fun onSuccess() {
                    Log.d("Picasso: load image success", url)
                }

                override fun onError(e: Exception?) {
                    e?.message?.let {
                        Log.e("Picasso: Error an load image", it)
                    }
                    e?.printStackTrace()
                }
            },
        )
    }
}
