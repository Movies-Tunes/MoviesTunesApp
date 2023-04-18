package com.myapplication.util

import androidx.recyclerview.widget.DiffUtil
import com.myapplication.domain.model.TopRatedMovie

class MoviesListDiffCallback : DiffUtil.ItemCallback<TopRatedMovie>() {
    override fun areItemsTheSame(
        oldItem: TopRatedMovie,
        newItem: TopRatedMovie,
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: TopRatedMovie,
        newItem: TopRatedMovie,
    ) = oldItem == newItem
}
