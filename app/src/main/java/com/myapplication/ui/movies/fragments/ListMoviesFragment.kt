package com.myapplication.ui.movies.fragments

import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.MoviesTunesApplication
import com.myapplication.R
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.databinding.FragmentListMoviesBinding
import com.myapplication.ui.movies.adapter.MoviesListAdapter
import com.myapplication.ui.movies.viewmodel.MoviesViewModel
import com.myapplication.ui.movies.viewmodel.ViewModelFactory
import com.myapplication.ui.util.TileDrawable

class ListMoviesFragment : Fragment() {

    private var _binding: FragmentListMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MoviesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
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
            requireContext(),
            RecyclerView.VERTICAL,
            false,
        ).also { binding.moviesListRv.layoutManager = it }

        val adapter = MoviesListAdapter { topRated: TopRatedResultItem ->
            val action =
                ListMoviesFragmentDirections.actionListMoviesFragmentToMovieDetailFragment(topRated)
            findNavController().navigate(action)
        }

        binding.moviesListRv.adapter = adapter
        val repository = (requireActivity().application as MoviesTunesApplication).movieDatasource

        viewModel =
            ViewModelProvider(this, ViewModelFactory(repository))[MoviesViewModel::class.java]

        viewModel.getMovieList().observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }

        LinearSnapHelper().attachToRecyclerView(binding.moviesListRv)
        setBackgroundRecyclerView()
    }

    private fun setBackgroundRecyclerView() {
        binding.filmRollLeft.setImageDrawable(
            TileDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.film_roll_piece,
                )!!,
                Shader.TileMode.REPEAT,
            ),
        )
        binding.filmRollRight.setImageDrawable(
            TileDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.film_roll_piece,
                )!!,
                Shader.TileMode.REPEAT,
            ),
        )
    }
}
