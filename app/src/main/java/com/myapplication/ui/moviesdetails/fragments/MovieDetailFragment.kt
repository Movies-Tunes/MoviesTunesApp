package com.myapplication.ui.moviesdetails.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.myapplication.R
import com.myapplication.core.Response
import com.myapplication.data.entities.MovieDetail
import com.myapplication.data.model.FavMovie
import com.myapplication.databinding.FragmentMovieDetailBinding
import com.myapplication.ui.favoritemovies.viewmodel.FavMoviesViewModel
import com.myapplication.ui.moviesdetails.viewmodel.MoviesDetailsViewModel
import com.myapplication.util.extension.snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {
    private lateinit var _binding: FragmentMovieDetailBinding
    val binding: FragmentMovieDetailBinding get() = _binding
    private val args: MovieDetailFragmentArgs by navArgs()
    private val moviesDetailsViewModel: MoviesDetailsViewModel by viewModels()
    private val favMoviesViewModel: FavMoviesViewModel by viewModels()

    @Inject lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.favViewModel = favMoviesViewModel
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setNavigationToolbar()
        initObservers()
        setListeners()
    }

    private fun initViews() {
        args.let { safeArguments ->
            getDataFromTopRated(safeArguments)
            getDataFromFavMovie(safeArguments.favMovie)
        }
    }

    private fun getDataFromTopRated(safeArguments: MovieDetailFragmentArgs) {
        safeArguments.topRated?.apply {
            _binding.topRatedMovie = this
            getDetailsMovie(id)
        }
    }

    private fun getDetailsMovie(id: Long) {
        id.let {
            moviesDetailsViewModel.getMovieDetails(it, Locale.getDefault().toLanguageTag())
            auth.currentUser?.uid?.let { uid ->
                favMoviesViewModel.isFavMovie(uid, it)
            }
        }.also {
            binding.layoutAddFavoriteMovie.ivStarFavorite.isEnabled = false
        }
    }

    private fun getDataFromFavMovie(favMovie: FavMovie?) {
        favMovie?.let {
            _binding.favMovie = it
            getDetailsMovie(it.id)
        }
    }

    private fun setNavigationToolbar() {
        _binding.detailToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initObservers() {
        moviesDetailsViewModel.moviesDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    _binding.tvDescriptionSinopse.text =
                        getString(R.string.error_loading_movie_details)
                }
                is Response.Success -> {
                    setDetailsInView(response.data)
                }
            }
        }
    }

    private fun setDetailsInView(movie: MovieDetail) {
        _binding.movieDetail = movie
    }

    private fun setListeners() {
        _binding.layoutContentDetailMovie.tvGenre.setOnClickListener {
            when (_binding.layoutContentDetailMovie.tvGenre.lineCount) {
                1 -> _binding.layoutContentDetailMovie.tvGenre.maxLines = Int.MAX_VALUE
                else -> _binding.layoutContentDetailMovie.tvGenre.maxLines = 1
            }
        }
        _binding.layoutAddFavoriteMovie.llFavoriteMovie.setOnClickListener {
            val ivStarFavorite = _binding.layoutAddFavoriteMovie.ivStarFavorite
            ivStarFavorite.isEnabled = !ivStarFavorite.isEnabled
            if (auth.currentUser != null) {
                _binding.movieDetail?.let {
                    val id = it.id
                    val path = it.posterPath!!
                    val userId = auth.currentUser!!.uid
                    val title = _binding.detailToolbar.title.toString()
                    if (ivStarFavorite.isEnabled) {
                        favMoviesViewModel.saveFavMovie(FavMovie(id, path, title, userId))
                        return@setOnClickListener
                    }
                    auth.currentUser?.uid?.let { uid -> favMoviesViewModel.deleteFavMovie(uid, id) }
                }
            } else {
                snackbar(message = getString(R.string.message_favorite_not_sign))
                navigateToLoginFragmennt()
            }
        }
    }

    private fun navigateToLoginFragmennt() {
        val action =
            MovieDetailFragmentDirections.actionMovieDetailFragmentToLoginFragment()
        findNavController().navigate(action)
    }
}
