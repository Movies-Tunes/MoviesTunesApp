package com.myapplication.util

import androidx.recyclerview.widget.DiffUtil
import com.myapplication.data.entities.TopRatedResultItem

class MoviesListDiffCallback : DiffUtil.ItemCallback<TopRatedResultItem>() {
    override fun areItemsTheSame(
        oldItem: TopRatedResultItem,
        newItem: TopRatedResultItem,
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: TopRatedResultItem,
        newItem: TopRatedResultItem,
    ) = oldItem == newItem
}
