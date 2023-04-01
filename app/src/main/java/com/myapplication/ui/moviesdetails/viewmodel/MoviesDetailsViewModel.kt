package com.myapplication.ui.moviesdetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapplication.core.Response
import com.myapplication.data.entities.MovieDetail
import com.myapplication.domain.repository.MovieRepository
import kotlinx.coroutines.launch

class MoviesDetailsViewModel(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private val _moviesDetails: MutableLiveData<Response<MovieDetail>> =
        MutableLiveData(Response.Loading())
    val moviesDetails: LiveData<Response<MovieDetail>> get() = _moviesDetails

    fun getMovieDetails(movieId: Long) {
        _moviesDetails.postValue(Response.Loading())
        viewModelScope.launch {
            val movieDetails = movieRepository.getMovieDetails(movieId)
            _moviesDetails.postValue(movieDetails)
        }
    }
}
