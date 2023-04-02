package com.myapplication.ui.movies.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.myapplication.core.Constants
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.domain.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private var _topRatedMovies: Flow<PagingData<TopRatedResultItem>> = emptyFlow()
    val topRatedMovies: Flow<PagingData<TopRatedResultItem>> get() = _topRatedMovies

    init {
      /*  val initialQuery: String =
            savedStateHandle[Constants.DEFAULT_QUERY] ?: Constants.DEFAULT_QUERY
        val lastQueryScrolled: String =
            savedStateHandle[Constants.DEFAULT_QUERY] ?: Constants.DEFAULT_QUERY
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val searches = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search(query = initialQuery)) }
        val queriesScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            // This is shared to keep the flow "hot" while caching the last query scrolled,
            // otherwise each flatMapLatest invocation would lose the last query scrolled,
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1,
            )
            .onStart { emit(UiAction.Scroll(currentQuery = lastQueryScrolled)) }*/
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

sealed class UiAction {
    data class Scroll(val currentQuery: String) : UiAction()
    data class Search(val query: String) : UiAction()
}

data class UiState(
    val query: String = Constants.DEFAULT_QUERY,
    val lastQueryScrolled: String = Locale.getDefault().country,
    val hasNotScrolledForCurrentSearch: Boolean = false,
)
