package com.myapplication.ui.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapplication.data.entities.MovieDetail
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.domain.repository.MovieRepository
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _topRatedMovies: MutableLiveData<List<TopRatedResultItem>> = MutableLiveData()
    val topRatedMovies: LiveData<List<TopRatedResultItem>> get() = _topRatedMovies

    private val _moviesDetails: MutableLiveData<MovieDetail> = MutableLiveData()
    val moviesDetails: LiveData<MovieDetail> get() = _moviesDetails

    fun getTopRatedMovies(page: Int) {
        viewModelScope.launch {
            val moviesList = movieRepository.getAllMovies(page)
            _topRatedMovies.postValue(moviesList)
        }
    }

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            val movieDetails = movieRepository.getMovieDetails(movieId)
            _moviesDetails.postValue(movieDetails)
        }
    }

}


