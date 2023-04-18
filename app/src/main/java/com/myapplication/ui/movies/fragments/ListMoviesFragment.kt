package com.myapplication.ui.movies.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.myapplication.databinding.FragmentListMoviesBinding
import com.myapplication.ui.movies.adapter.MoviesListAdapter
import com.myapplication.ui.movies.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListMoviesFragment : Fragment() {

    private lateinit var _binding: FragmentListMoviesBinding
    private val binding get() = _binding

    @Inject
    lateinit var auth: FirebaseAuth
    private val movieViewModel: MoviesViewModel by viewModels()
    private lateinit var moviesListAdapter: MoviesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentListMoviesBinding.inflate(layoutInflater)
        _binding.movieListViewModel = movieViewModel
        _binding.auth = auth
        _binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        initObserversOfView()
        setListeners()
    }
    private fun setRecyclerView() {
        MoviesListAdapter(requireContext()) { topRated ->
            val action =
                ListMoviesFragmentDirections.actionListMoviesFragmentToMovieDetailFragment(topRated)
            findNavController().navigate(action)
        }.also {
            binding.moviesListRv.adapter = it
            moviesListAdapter = it
        }

        binding.moviesListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                binding.fabTurnToTop.isVisible = newState != 0
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun initObserversOfView() {
        lifecycleScope.launch {
            moviesListAdapter.loadStateFlow.collect { states ->
                binding.moviesListRv.isVisible =
                    states.refresh !is LoadState.Loading ||
                    states.refresh is LoadState.NotLoading
                binding.tvErrorLoading.isVisible = states.refresh is LoadState.Error
                binding.pbLoadingList.isVisible = states.refresh is LoadState.Loading
            }
        }
        lifecycleScope.launch {
            movieViewModel.topRatedMovies.collectLatest {
                moviesListAdapter = _binding.moviesListRv.adapter as MoviesListAdapter
                moviesListAdapter.submitData(it)
            }
        }
    }

    private fun setListeners() {
        _binding.tvErrorLoading.setOnClickListener { moviesListAdapter.retry() }
        _binding.fabTurnToTop.setOnClickListener {
            _binding.moviesListRv.scrollToPosition(0)
        }
        _binding.ivLogin.setOnClickListener {
            val action =
                ListMoviesFragmentDirections.actionListMoviesFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        _binding.ivSignOut.setOnClickListener {
            auth.signOut()
            val action =
                ListMoviesFragmentDirections.actionListMoviesFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        _binding.fabPickYourFavorites.setOnClickListener {
            findNavController().navigate(
                ListMoviesFragmentDirections.actionListMoviesFragmentToFavMoviesFragment(),
            )
        }
    }
}
