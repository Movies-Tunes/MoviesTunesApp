package com.myapplication.util.bindingadapter

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.snackbar.Snackbar
import com.myapplication.R
import com.myapplication.core.Response
import com.myapplication.data.entities.GenreItem
import com.myapplication.util.extension.gone
import com.myapplication.util.extension.visible

@BindingAdapter("bind_genres")
fun bindGenre(textView: TextView, genres: List<GenreItem>?) {
    textView.text = genres?.joinToString(limit = 3) { genreItem -> genreItem.name }
}

@BindingAdapter(value = ["fav_state"], requireAll = false)
fun bindIsSuccesfulTask(
    imageView: AppCompatImageView?,
    response: Response<Boolean>?
) {
    response?.let {
        when (response) {
            is Response.Error -> {
                response.exception.printStackTrace()
            }
            is Response.Success -> {
                imageView?.let {
                    val snackbar = Snackbar.make(imageView, response.message, Snackbar.LENGTH_LONG)
                    snackbar
                        .setAction(R.string.message_dismiss_snackbar) {
                            snackbar.dismiss()
                        }
                        .show()
                }
            }
        }
    }
}

@BindingAdapter("is_fav_movie")
fun bindIsFavMovie(ivStarFavorite: ImageView, response: Response<Boolean>?) {
    response?.let {
        when (response) {
            is Response.Error -> {
                ivStarFavorite.isEnabled = false
                response.exception.printStackTrace()
                response.exception.message?.let { it1 ->
                    Snackbar.make(
                        ivStarFavorite,
                        it1,
                        Snackbar.LENGTH_LONG,
                    )
                }?.also { snackbar ->
                    snackbar.setAction(R.string.message_dismiss_snackbar) {
                        snackbar.dismiss()
                    }
                        .show()
                }
            }
            is Response.Success -> {
                ivStarFavorite.isEnabled = !response.data
            }
        }
    }
}
