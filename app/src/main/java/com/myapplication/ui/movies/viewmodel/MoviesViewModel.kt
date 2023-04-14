package com.myapplication.ui.movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
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
