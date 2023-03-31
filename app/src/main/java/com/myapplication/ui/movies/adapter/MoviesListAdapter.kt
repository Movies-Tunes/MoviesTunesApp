package com.myapplication.ui.movies.adapter

import android.content.Context
import android.graphics.Shader
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.R
import com.myapplication.core.Constants.BASE_POSTER_PATH
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.databinding.MovieItemBinding
import com.myapplication.ui.util.TileDrawable
import com.myapplication.util.MoviesListDiffCallback
import com.myapplication.util.extension.concatParam
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class MoviesListAdapter(
    private val context: Context,
    private val onClickItem: (TopRatedResultItem) -> Unit,
) : PagingDataAdapter<TopRatedResultItem, MoviesListAdapter.MoviesListViewHolder>(
    MoviesListDiffCallback(),
) {
    inner class MoviesListViewHolder(
        private val binding: MovieItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: TopRatedResultItem) {
            binding.movieTitle.text =
                binding.root.context.getString(R.string.movie_title, movie.title)
            Picasso.get().load(
                BASE_POSTER_PATH.concatParam(movie.posterPath),
            ).placeholder(R.drawable.rotate_loading).into(
                binding.movieIv,
                object : Callback {
                    override fun onSuccess() {
                        println("sucess Picasso")
                    }

                    override fun onError(e: Exception?) {
                        println("erro Picasso: $e")
                    }
                },
            )
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
        }

        private fun setListeners(movie: TopRatedResultItem) {
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
