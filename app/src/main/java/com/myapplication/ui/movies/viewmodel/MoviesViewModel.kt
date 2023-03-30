package com.myapplication.ui.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.myapplication.core.Response
import com.myapplication.data.entities.MovieDetail
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.domain.repository.MovieRepository
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _topRatedMovies: MutableLiveData<Response<List<TopRatedResultItem>>> =
        MutableLiveData(Response.Loading())
    val topRatedMovies: LiveData<Response<List<TopRatedResultItem>>> get() = _topRatedMovies

    private val _moviesDetails: MutableLiveData<Response<MovieDetail>> =
        MutableLiveData(Response.Loading())
    val moviesDetails: LiveData<Response<MovieDetail>> get() = _moviesDetails


    fun getMovieList(): LiveData<PagingData<TopRatedResultItem>> {
        return movieRepository.getAllMovies().cachedIn(viewModelScope)
    }

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


