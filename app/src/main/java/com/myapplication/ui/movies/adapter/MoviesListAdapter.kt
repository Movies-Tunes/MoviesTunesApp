package com.myapplication.ui.movies.adapter

import android.content.Context
import android.graphics.Shader
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.R
import com.myapplication.databinding.MovieItemBinding
import com.myapplication.domain.model.TopRatedMovie
import com.myapplication.ui.util.TileDrawable
import com.myapplication.util.MoviesListDiffCallback

class MoviesListAdapter(
    private val context: Context,
    private val onClickItem: (TopRatedMovie) -> Unit,
) : PagingDataAdapter<TopRatedMovie, MoviesListAdapter.MoviesListViewHolder>(
    MoviesListDiffCallback(),
) {
    inner class MoviesListViewHolder(
        private val binding: MovieItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: TopRatedMovie) {
            binding.topRated = movie
            binding.filmRollLeft.setImageDrawable(
                TileDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.film_roll_piece,
                    )!!,
                    Shader.TileMode.REPEAT,
                ),
            )
            binding.filmRollRight.setImageDrawable(
                TileDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.film_roll_piece,
                    )!!,
                    Shader.TileMode.REPEAT,
                ),
            )
            setListeners(movie)
            binding.executePendingBindings()
        }

        private fun setListeners(movie: TopRatedMovie) {
            binding.root.setOnClickListener {
                onClickItem(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesListViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MoviesListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesListViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(movie)
        }
    }
}
