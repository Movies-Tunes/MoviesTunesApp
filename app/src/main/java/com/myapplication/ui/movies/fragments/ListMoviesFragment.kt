package com.myapplication.ui.movies.fragments

import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.R
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.databinding.FragmentListMoviesBinding
import com.myapplication.ui.movies.adapter.ListMoviesAdapter
import com.myapplication.ui.util.TileDrawable

class ListMoviesFragment : Fragment() {

    private var _binding: FragmentListMoviesBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListMoviesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LinearLayoutManager(
            requireContext(), RecyclerView.VERTICAL, false
        ).also { binding.moviesListRv.layoutManager = it }

        ListMoviesAdapter(
            mutableListOf(
                TopRatedResultItem(
                    id = 238,
                    posterPath = "https://image.tmdb.org/t/p/w342/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
                    title = "The Godfather"
                ), TopRatedResultItem(
                    id = 278,
                    posterPath = "https://image.tmdb.org/t/p/w342/6gIJuFHh5Lj4dNaPG3TzIMl7L68.jpg",
                    title = "Cuando Sea Joven"
                ), TopRatedResultItem(
                    id = 240,
                    posterPath = "https://image.tmdb.org/t/p/w342/hek3koDUyRQk7FIhPXsa6mT2Zc3.jpg",
                    title = "The Godfather Part II"
                ), TopRatedResultItem(
                    id = 240,
                    posterPath = "https://image.tmdb.org/t/p/w342/hek3koDUyRQk7FIhPXsa6mT2Zc3.jpg",
                    title = "The Godfather Part II"
                ), TopRatedResultItem(
                    id = 240,
                    posterPath = "https://image.tmdb.org/t/p/w342/hek3koDUyRQk7FIhPXsa6mT2Zc3.jpg",
                    title = "The Godfather Part II"
                ), TopRatedResultItem(
                    id = 240,
                    posterPath = "https://image.tmdb.org/t/p/w342/hek3koDUyRQk7FIhPXsa6mT2Zc3.jpg",
                    title = "The Godfather Part II"
                )
            )

        ).also { binding.moviesListRv.adapter = it }
        LinearSnapHelper().attachToRecyclerView(binding.moviesListRv)

        binding.filmRollLeft.setImageDrawable(
            TileDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.film_roll_piece
                )!!, Shader.TileMode.REPEAT
            )
        )
        binding.filmRollRight.setImageDrawable(
            TileDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.film_roll_piece
                )!!, Shader.TileMode.REPEAT
            )
        )
    }
}