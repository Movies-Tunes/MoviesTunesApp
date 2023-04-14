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
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.myapplication.core.Response
import com.myapplication.data.model.FavMovie
import com.myapplication.databinding.FragmentFavMoviesBinding
import com.myapplication.ui.favoritemovies.adapter.FavMoviesAdapter
import com.myapplication.ui.favoritemovies.viewmodel.FavMoviesViewModel
import com.myapplication.util.extension.createLoadingDialog
import com.myapplication.util.extension.gone
import com.myapplication.util.extension.snackbar
import com.myapplication.util.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavMoviesFragment : Fragment() {

    private lateinit var _binding: FragmentFavMoviesBinding
    val binding: FragmentFavMoviesBinding get() = _binding

    @Inject
    lateinit var auth: FirebaseAuth
    private val loadingDialog: Dialog by lazy { createLoadingDialog() }

    private val favViewModel: FavMoviesViewModel by viewModels()

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
            state?.let { safeState ->
                when (safeState) {
                    is Response.Error -> {
                        loadingDialog.hide()
                        safeState.exception.apply {
                            message?.let {
                                snackbar(message = it)
                            }
                            printStackTrace()
                        }
                    }
                    is Response.Loading -> {
                        loadingDialog.show()
                    }
                    is Response.Success -> {
                        loadingDialog.hide()
                        Log.e("data", safeState.data.toString())
                        setFavMoviesInView(safeState.data)
                    }
                }
            }
        }
        favViewModel.isSuccessfullTask.observe(viewLifecycleOwner){ state ->
            state?.let { safeState ->
                when (safeState) {
                    is Response.Error -> {
                        loadingDialog.hide()
                        safeState.exception.apply {
                            message?.let {
                                snackbar(message = it)
                            }
                            printStackTrace()
                        }
                    }
                    is Response.Loading -> {
                        loadingDialog.show()
                    }
                    is Response.Success -> {
                        loadingDialog.hide()
                        Log.e("data", safeState.data.toString())
                        snackbar(message = getString(safeState.message))
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        auth.currentUser?.uid?.let { favViewModel.getFavMovies(it) }
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

    private fun setFavMoviesInView(movies: List<FavMovie?>) {
        val favMoviesAdapter = _binding.rvFavFilms.adapter as FavMoviesAdapter
        if (movies.isNotEmpty()) {
            favMoviesAdapter.submitList(movies)
            _binding.tvNotFoundFav.gone()
            _binding.rvFavFilms.visible()
        }
    }
}
