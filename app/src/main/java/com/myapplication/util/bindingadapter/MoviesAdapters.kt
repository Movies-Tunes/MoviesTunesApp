package com.myapplication.util.bindingadapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.myapplication.core.Response
import com.myapplication.data.model.FavMovie
import com.myapplication.ui.favoritemovies.adapter.FavMoviesAdapter
import com.myapplication.util.extension.gone
import com.myapplication.util.extension.visible


@BindingAdapter("recycler_data")
fun bindList(recyclerView: RecyclerView, response: Response<List<FavMovie>>?) {
    response?.let {
        when (response) {
            is Response.Error -> {
                recyclerView.gone()
                response.exception.printStackTrace()
                response.exception.message?.let {
                    Snackbar.make(recyclerView, it, Snackbar.LENGTH_LONG).show()
                }
            }

            is Response.Success -> {
                recyclerView.visible()
                val adapter = recyclerView.adapter as FavMoviesAdapter
                adapter.submitList(response.data)
            }
        }
    }
}
