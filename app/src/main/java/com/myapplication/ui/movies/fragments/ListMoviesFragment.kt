package com.myapplication.ui.movies.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.MoviesTunesApplication
import com.myapplication.databinding.FragmentListMoviesBinding
import com.myapplication.ui.movies.adapter.MoviesListAdapter
import com.myapplication.ui.movies.viewmodel.MoviesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListMoviesFragment : Fragment() {

    private lateinit var _binding: FragmentListMoviesBinding
    private val binding get() = _binding
    private val movieViewModel: MoviesViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory() {
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle,
            ): T {
                val application = (activity?.application as MoviesTunesApplication)
                return MoviesViewModel(handle, application.movieDatasource) as T
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
        initObserversOfView()
        setListeners()
    }

    private fun setRecyclerView() {
        LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false,
        ).also { binding.moviesListRv.layoutManager = it }

        MoviesListAdapter(requireContext()) { topRated ->
            val action =
                ListMoviesFragmentDirections.actionListMoviesFragmentToMovieDetailFragment(topRated)
            findNavController().navigate(action)
        }.also { binding.moviesListRv.adapter = it }
        binding.moviesListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                binding.fabTurnToTop.isVisible = newState != 0
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun initObserversOfView() {
        collectTopRatedMovies()
        lifecycleScope.launch {
            val moviesListAdapter = _binding.moviesListRv.adapter as MoviesListAdapter
            moviesListAdapter.loadStateFlow.collect { states ->
                binding.moviesListRv.isVisible =
                    states.refresh !is LoadState.Loading ||
                    states.refresh is LoadState.NotLoading
                binding.tvErrorLoading.isVisible = states.refresh is LoadState.Error
                binding.pbLoadingList.isVisible = states.refresh is LoadState.Loading
            }
        }
    }

    private fun collectTopRatedMovies() {
        lifecycleScope.launch(Dispatchers.IO) {
            movieViewModel.topRatedMovies.collectLatest {
                val moviesListAdapter = _binding.moviesListRv.adapter as MoviesListAdapter
                moviesListAdapter.submitData(it)
            }
        }
    }

    private fun setListeners() {
        _binding.tvErrorLoading.setOnClickListener {
            val moviesListAdapter = _binding.moviesListRv.adapter as MoviesListAdapter
            moviesListAdapter.retry()
        }
        _binding.fabTurnToTop.setOnClickListener {
            _binding.moviesListRv.smoothScrollToPosition(0)
        }
    }
}
