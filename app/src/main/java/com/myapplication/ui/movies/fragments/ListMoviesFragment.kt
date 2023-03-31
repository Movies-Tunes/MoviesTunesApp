package com.myapplication.ui.movies.fragments

import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.MoviesTunesApplication
import com.myapplication.R
import com.myapplication.core.Response
import com.myapplication.databinding.FragmentListMoviesBinding
import com.myapplication.ui.movies.adapter.MoviesListAdapter
import com.myapplication.ui.movies.viewmodel.MoviesViewModel
import com.myapplication.ui.util.TileDrawable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListMoviesFragment : Fragment() {

    private lateinit var _binding: FragmentListMoviesBinding
    private val binding get() = _binding
    private val movieViewModel: MoviesViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val application = (activity?.application as MoviesTunesApplication)
                return MoviesViewModel(application.movieDatasource) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentListMoviesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setBackgroundRecyclerView()
        initObserversOfView()
        setListeners()
        movieViewModel.getMovieList()
        lifecycleScope.launch {
            movieViewModel.flow.collectLatest {
                (_binding.moviesListRv.adapter as MoviesListAdapter)
                    .submitData(it)
            }
        }
    }

    private fun setRecyclerView() {
        LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false,
        ).also { binding.moviesListRv.layoutManager = it }

        MoviesListAdapter { topRated ->
            val action =
                ListMoviesFragmentDirections.actionListMoviesFragmentToMovieDetailFragment(topRated)
            findNavController().navigate(action)
        }.also { binding.moviesListRv.adapter = it }
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

    private fun initObserversOfView() {
        lifecycleScope.launch {
            movieViewModel.topRatedMovies.collectLatest { response ->
                when (response) {
                    is Response.Error -> {
                        setErrorState()
                        response.exception.printStackTrace()
                    }
                    is Response.Loading -> {
                        setInitialVisibleStates()
                    }
                    is Response.Success -> {
                        val moviesListAdapter = _binding.moviesListRv.adapter as MoviesListAdapter
                        moviesListAdapter.submitData(lifecycle, response.data)
                        setCompleteLoadingState()
                    }
                }
            }
        }
    }

    private fun setErrorState() {
        _binding.moviesListLayout.visibility = View.GONE
        _binding.tvErrorLoading.visibility = View.VISIBLE
        _binding.pbLoadingList.visibility = View.GONE
    }

    private fun setInitialVisibleStates() {
        _binding.moviesListLayout.visibility = View.GONE
        _binding.tvErrorLoading.visibility = View.GONE
        _binding.pbLoadingList.visibility = View.VISIBLE
    }

    private fun setCompleteLoadingState() {
        _binding.moviesListLayout.visibility = View.VISIBLE
        _binding.tvErrorLoading.visibility = View.GONE
        _binding.pbLoadingList.visibility = View.GONE
    }

    private fun setListeners() {
        _binding.tvErrorLoading.setOnClickListener {
            movieViewModel.getMovieList()
        }
    }
}
