package com.myapplication.ui.moviesdetails.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.myapplication.MoviesTunesApplication
import com.myapplication.R
import com.myapplication.core.Constants
import com.myapplication.core.Response
import com.myapplication.data.entities.MovieDetail
import com.myapplication.databinding.FragmentMovieDetailBinding
import com.myapplication.ui.moviesdetails.viewmodel.MoviesDetailsViewModel
import com.myapplication.util.extension.concatParam
import com.squareup.picasso.Picasso

/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieDetailFragment : Fragment() {
    private lateinit var _binding: FragmentMovieDetailBinding
    val binding: FragmentMovieDetailBinding get() = _binding
    private val args: MovieDetailFragmentArgs by navArgs()
    private val moviesDetailsViewModel: MoviesDetailsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val application = activity?.application as MoviesTunesApplication
                return MoviesDetailsViewModel(application.movieDatasource) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
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
            val rated = safeArguments.topRated
            val id = rated?.id
            val posterPath = rated?.posterPath
            val title = rated?.title
            val result = "$id $title $posterPath"
            posterPath?.let { safePath ->
                Picasso.get()
                    .load(Constants.BASE_POSTER_PATH.concatParam(safePath))
                    .into(_binding.ivPoster)
            }
            _binding.detailToolbar.title = title
            id?.let {
                moviesDetailsViewModel.getMovieDetails(it)
            }
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
                    _binding.pbLoadingDetails.visibility = View.GONE
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
    }

    private fun setDetailsInView(movie: MovieDetail) {
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
        _binding.tvDescriptionSinopse.visibility = View.VISIBLE
        _binding.pbLoadingDetails.visibility = View.GONE
    }

    private fun setListeners() {
        _binding.tvGenre.setOnClickListener {
            when(_binding.tvGenre.lineCount) {
                1 -> _binding.tvGenre.maxLines = Int.MAX_VALUE
                else -> _binding.tvGenre.maxLines = 1
            }
        }
    }
}
