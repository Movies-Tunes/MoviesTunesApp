package com.myapplication.ui.movies.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.domain.repository.MovieRepository
import kotlinx.coroutines.flow.*
import java.util.*

class MoviesViewModel(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private var _topRatedMovies: Flow<PagingData<TopRatedResultItem>> = emptyFlow()
    val topRatedMovies: Flow<PagingData<TopRatedResultItem>> get() = _topRatedMovies

    init {
        _topRatedMovies = getMovieList(Locale.getDefault().toLanguageTag())
            .cachedIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        _topRatedMovies = movieRepository
            .getAllMovies(Locale.getDefault().toLanguageTag())
            .cachedIn(viewModelScope)
    }

    private fun getMovieList(query: String): Flow<PagingData<TopRatedResultItem>> {
        return movieRepository.getAllMovies(query)
    }
}
