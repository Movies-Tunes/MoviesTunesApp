package com.myapplication.ui.movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.data.mappers.mapToTopRated
import com.myapplication.domain.model.TopRatedMovie
import com.myapplication.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private var _topRatedMovies: Flow<PagingData<TopRatedMovie>> =
        getMovieList(Locale.getDefault().toLanguageTag())
            .map { value: PagingData<TopRatedResultItem> ->
                value.map { it.mapToTopRated() }
            }.cachedIn(viewModelScope)
    val topRatedMovies: Flow<PagingData<TopRatedMovie>> get() = _topRatedMovies

    override fun onCleared() {
        super.onCleared()
        _topRatedMovies =
            getMovieList(Locale.getDefault().toLanguageTag())
                .map { value: PagingData<TopRatedResultItem> ->
                    value.map { it.mapToTopRated() }
                }.cachedIn(viewModelScope)
    }

    private fun getMovieList(query: String): Flow<PagingData<TopRatedResultItem>> {
        return movieRepository.getAllMovies(query)
    }
}
