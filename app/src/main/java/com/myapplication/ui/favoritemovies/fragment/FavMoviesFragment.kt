package com.myapplication.ui.favoritemovies.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myapplication.MoviesTunesApplication
import com.myapplication.R
import com.myapplication.core.Response
import com.myapplication.data.model.FavMovie
import com.myapplication.databinding.FragmentFavMoviesBinding
import com.myapplication.ui.favoritemovies.adapter.FavMoviesAdapter
import com.myapplication.ui.favoritemovies.viewmodel.FavMoviesViewModel
import com.myapplication.util.extension.snackbar

class FavMoviesFragment : Fragment() {

    private lateinit var _binding: FragmentFavMoviesBinding
    val binding: FragmentFavMoviesBinding get() = _binding
    val auth: FirebaseAuth = Firebase.auth
    private var loadingDialog: Dialog? = null

    private val favViewModel: FavMoviesViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val firestoreCollection =
                    (activity?.application as MoviesTunesApplication).firestoreCollection
                return FavMoviesViewModel(firestoreCollection) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavMoviesBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        initObservers()
    }

    private fun initObservers() {
        favViewModel.favMovies.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Response.Error -> {
                    hideLoading()
                    state.exception.apply {
                        message?.let {
                            snackbar(message = it)
                        }
                        printStackTrace()
                    }
                }
                is Response.Loading -> {
                    showLoading()
                }
                is Response.Success -> {
                    hideLoading()
                    Log.e("data", state.data.toString())
                    setFavMoviesInView(state.data)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        auth.currentUser?.uid?.let { favViewModel.getFavMovies(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (loadingDialog != null) {
            loadingDialog = null
        }
    }

    private fun setView() {
        _binding.rvFavFilms.adapter = FavMoviesAdapter {
            val action =
                FavMoviesFragmentDirections.actionFavMoviesFragmentToMovieDetailFragment(
                    favMovie = it,
                )
            findNavController().navigate(action)
        }
        _binding.rvFavFilms.setHasFixedSize(true)
        _binding.toolbarFav.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showLoading() {
        loadingDialog = Dialog(requireContext())
        loadingDialog?.setContentView(R.layout.progress_dialog)
        loadingDialog?.show()
    }

    private fun hideLoading() {
        loadingDialog?.hide()
    }

    private fun setFavMoviesInView(movies: List<FavMovie?>) {
        val favMoviesAdapter = _binding.rvFavFilms.adapter as FavMoviesAdapter
        if (movies.isNotEmpty()) {
            favMoviesAdapter.submitList(movies)
            _binding.tvNotFoundFav.isVisible = false
            _binding.rvFavFilms.isVisible = true
        }
    }
}
