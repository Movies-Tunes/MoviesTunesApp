package com.myapplication.util.bindingadapter

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.myapplication.core.Response
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.data.model.FavMovie
import com.myapplication.ui.favoritemovies.adapter.FavMoviesAdapter
import com.myapplication.ui.movies.adapter.MoviesListAdapter
import com.myapplication.util.extension.gone
import com.myapplication.util.extension.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@BindingAdapter("list_data")
fun bindMoviesList(recyclerView: RecyclerView, moviesList: Flow<PagingData<TopRatedResultItem>>) {
    val moviesListAdapter = recyclerView.adapter as MoviesListAdapter
    CoroutineScope(Dispatchers.IO).launch {
        moviesList.collectLatest {
            Log.e("data", it.toString())
            moviesListAdapter.submitData(it)
        }
    }
}

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
