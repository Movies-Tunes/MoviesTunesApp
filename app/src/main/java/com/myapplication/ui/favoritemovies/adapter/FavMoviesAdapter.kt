package com.myapplication.ui.favoritemovies.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.R
import com.myapplication.core.Constants
import com.myapplication.data.model.FavMovie
import com.myapplication.databinding.ItemMovieFavBinding
import com.myapplication.util.extension.concatParam
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class FavMoviesAdapter(
    private val onClickItem: (FavMovie) -> Unit,
) :
    ListAdapter<FavMovie, FavMoviesAdapter.FavMovieViewHolder>(DiffUtilCallback) {
    inner class FavMovieViewHolder(private val binding: ItemMovieFavBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: FavMovie) {
            binding.favMovie = movie
            setListeners(movie)
            binding.executePendingBindings()
        }

        private fun setListeners(movie: FavMovie) {
            binding.ivPosterFav.setOnClickListener {
                onClickItem(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavMovieViewHolder {
        val binding =
            ItemMovieFavBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavMovieViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    object DiffUtilCallback : DiffUtil.ItemCallback<FavMovie>() {
        override fun areItemsTheSame(oldItem: FavMovie, newItem: FavMovie): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: FavMovie, newItem: FavMovie): Boolean =
            oldItem.id == newItem.id
    }
}
