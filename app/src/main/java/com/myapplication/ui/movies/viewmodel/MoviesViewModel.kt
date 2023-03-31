package com.myapplication.ui.movies.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.myapplication.core.Response
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private val _topRatedMovies: MutableStateFlow<Response<PagingData<TopRatedResultItem>>> =
        MutableStateFlow(Response.Loading())
    val topRatedMovies: StateFlow<Response<PagingData<TopRatedResultItem>>> get() = _topRatedMovies
    val flow = movieRepository.getAllMovies().cachedIn(viewModelScope)

    fun getMovieList() {
        viewModelScope.launch {
            flow
                .collectLatest { value: PagingData<TopRatedResultItem> ->
                    value.let {
                        _topRatedMovies.value = Response.Success(value)
                    }
                }
        }
    }
}
