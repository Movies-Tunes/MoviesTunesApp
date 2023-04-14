package com.myapplication.ui.moviesdetails.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.myapplication.R
import com.myapplication.core.Constants
import com.myapplication.core.Response
import com.myapplication.data.entities.MovieDetail
import com.myapplication.data.model.FavMovie
import com.myapplication.databinding.FragmentMovieDetailBinding
import com.myapplication.ui.favoritemovies.viewmodel.FavMoviesViewModel
import com.myapplication.ui.moviesdetails.viewmodel.MoviesDetailsViewModel
import com.myapplication.util.extension.concatParam
import com.myapplication.util.extension.snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {
    private lateinit var _binding: FragmentMovieDetailBinding
    val binding: FragmentMovieDetailBinding get() = _binding
    private val args: MovieDetailFragmentArgs by navArgs()
    private var movieDetail: MovieDetail? = null
    private val moviesDetailsViewModel: MoviesDetailsViewModel by viewModels()
    private val favMovies: FavMoviesViewModel by viewModels()

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
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
            val id = id
            val posterPath = posterPath
            val title = title
            posterPath.let { safePath ->
                Picasso.get()
                    .load(Constants.BASE_POSTER_PATH.concatParam(safePath))
                    .into(_binding.ivPoster)
            }
            _binding.detailToolbar.title = title
            getDetailsMovie(id)
        }
    }

    private fun getDetailsMovie(id: Long) {
        id.let {
            moviesDetailsViewModel.getMovieDetails(it, Locale.getDefault().toLanguageTag())
            lifecycleScope.launch {
                _binding.ivStarFavorite.isEnabled =
                    auth.currentUser?.uid?.let { uid ->
                        favMovies.isFavMovie(uid, it).not()
                    } == true
            }
        }
    }

    private fun getDataFromFavMovie(favMovie: FavMovie?) {
        favMovie?.let {
            _binding.detailToolbar.title = it.title
            it.posterPath.let { safePath ->
                Picasso.get()
                    .load(Constants.BASE_POSTER_PATH.concatParam(safePath))
                    .into(_binding.ivPoster)
            }
            getDetailsMovie(it.id)
        }
    }

    private fun setNavigationToolbar() {
        _binding.detailToolbar.setNavigationIcon(R.drawable.ic_back)
        _binding.detailToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initObservers() {
        moviesDetailsViewModel.moviesDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    _binding.pbLoadingDetails.isVisible = false
                    _binding.tvDescriptionSinopse.text = getString(R.string.error_loading_movie_details)
                }
                is Response.Loading -> {
                    setInitialVisibleStates()
                }
                is Response.Success -> {
                    setCompleteLoadingState()
                    setDetailsInView(response.data)
                }
            }
        }
        favMovies.favMovies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Error -> {
                    _binding.pbLoadingDetails.isVisible = false
                    _binding.tvDescriptionSinopse.text =
                        getString(R.string.error_loading_movie_details)
                }
                is Response.Loading -> {
                    setInitialVisibleStates()
                }
                is Response.Success -> {
                    setCompleteLoadingState()
                    snackbar(message = getString(response.message))
                }
            }
        }
    }

    private fun setDetailsInView(movie: MovieDetail) {
        movieDetail = movie
        _binding.tvDescriptionSinopse.text = movie.overview
        _binding.tvYear.text = movie.releaseDate
        _binding.tvGenre.text = movie.genres.joinToString(limit = 3) { genreItem ->
            genreItem.name
        }
        _binding.tvDuration.text = movie.runtime.toString()
    }

    private fun setInitialVisibleStates() {
        _binding.tvDescriptionSinopse.visibility = View.GONE
        _binding.pbLoadingDetails.visibility = View.VISIBLE
    }

    private fun setCompleteLoadingState() {
        _binding.tvDescriptionSinopse.isVisible = true
        _binding.pbLoadingDetails.isVisible = false
        _binding.llFavoriteMovie.isVisible = true
    }

    private fun setListeners() {
        _binding.tvGenre.setOnClickListener {
            when (_binding.tvGenre.lineCount) {
                1 -> _binding.tvGenre.maxLines = Int.MAX_VALUE
                else -> _binding.tvGenre.maxLines = 1
            }
        }
        _binding.llFavoriteMovie.setOnClickListener {
            _binding.ivStarFavorite.isEnabled = !_binding.ivStarFavorite.isEnabled
            Log.e("enabled", _binding.ivStarFavorite.isEnabled.toString())
            if (auth.currentUser != null) {
                movieDetail?.let {
                    val id = it.id
                    val path = it.posterPath!!
                    val userId = auth.currentUser!!.uid
                    val title = _binding.detailToolbar.title.toString()
                    if (_binding.ivStarFavorite.isEnabled) {
                        favMovies.saveFavMovie(
                            FavMovie(id, path, title, userId),
                        )
                        return@setOnClickListener
                    }
                    auth.currentUser?.uid?.let { it1 -> favMovies.deleteFavMovie(it1, id) }
                }
            } else {
                snackbar(
                    message = getString(R.string.message_favorite_not_sign),
                )
                val action =
                    MovieDetailFragmentDirections.actionMovieDetailFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }
    }
}
