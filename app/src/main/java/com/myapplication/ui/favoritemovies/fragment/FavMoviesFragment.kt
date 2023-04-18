package com.myapplication.ui.favoritemovies.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.myapplication.core.Response
import com.myapplication.databinding.FragmentFavMoviesBinding
import com.myapplication.ui.favoritemovies.adapter.FavMoviesAdapter
import com.myapplication.ui.favoritemovies.viewmodel.FavMoviesViewModel
import com.myapplication.util.extension.createLoadingDialog
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
        _binding.viewModel = favViewModel
        _binding.lifecycleOwner = viewLifecycleOwner
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        initObservers()
    }

    private fun initObservers() {
        favViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) loadingDialog.show() else loadingDialog.dismiss()
        }
        favViewModel.favMovies.observe(viewLifecycleOwner) {
            it?.let { response ->
                when (response) {
                    is Response.Error -> {
                        response.exception.printStackTrace()
                        response.exception.message?.let { message ->
                            snackbar(message = message)
                        }
                    }

                    is Response.Success -> {
                        if (response.data.isEmpty()) binding.tvNotFoundFav.visible()
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
}
