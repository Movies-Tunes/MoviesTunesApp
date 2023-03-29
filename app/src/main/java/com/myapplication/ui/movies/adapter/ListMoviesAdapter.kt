package com.myapplication.ui.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.R
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.databinding.MovieItemBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ListMoviesAdapter(private val moviesList: MutableList<TopRatedResultItem>) :
    RecyclerView.Adapter<ListMoviesAdapter.ListMovieViewHolder>() {


    class ListMovieViewHolder(
        private val binding: MovieItemBinding

    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: TopRatedResultItem) {
            binding.movieTitle.text = binding.root.context.getString(R.string.movie_title, movie.title)
            Picasso.get().load(movie.posterPath).into(binding.movieIv, object : Callback {
                override fun onSuccess() {
                    println("sucess Picasso")
                }

                override fun onError(e: Exception?) {
                    println("erro Picasso: $e")
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListMovieViewHolder, position: Int) {
        holder.bind(moviesList[position])
    }

    override fun getItemCount(): Int = moviesList.size


}