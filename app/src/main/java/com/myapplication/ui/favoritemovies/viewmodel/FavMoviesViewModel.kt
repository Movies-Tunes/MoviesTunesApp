package com.myapplication.ui.favoritemovies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapplication.core.Response
import com.myapplication.data.model.FavMovie
import com.myapplication.domain.usecases.favmovies.DeleteFavUseCase
import com.myapplication.domain.usecases.favmovies.GetFavMoviesUseCase
import com.myapplication.domain.usecases.favmovies.IsFavMovieUseCase
import com.myapplication.domain.usecases.favmovies.SaveFavMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavMoviesViewModel @Inject constructor(
    private val getFavMoviesUseCase: GetFavMoviesUseCase,
    private val isFavMovieUseCase: IsFavMovieUseCase,
    private val saveFavMoviesUseCase: SaveFavMoviesUseCase,
    private val deleteFavUseCase: DeleteFavUseCase,
) : ViewModel() {
    private val _favMovies: MutableLiveData<Response<List<FavMovie>>?> =
        MutableLiveData()
    val favMovies: LiveData<Response<List<FavMovie>>?> = _favMovies
    private val _isFavMovie: MutableLiveData<Response<Boolean>?> =
        MutableLiveData()
    val isFavMovie: LiveData<Response<Boolean>?> = _isFavMovie
    private val _isSuccessfullTask: MutableLiveData<Response<Boolean>?> =
        MutableLiveData()
    val isSuccessfullTask: LiveData<Response<Boolean>?> = _isSuccessfullTask
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    fun isFavMovie(userId: String, movieId: Long) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            _isFavMovie.value = isFavMovieUseCase(userId, movieId)
            _isLoading.postValue(false)
        }
    }

    fun getFavMovies(userId: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            val movies = getFavMoviesUseCase(userId)
            _favMovies.value = movies
            _isLoading.postValue(false)
        }
    }

    fun saveFavMovie(favMovie: FavMovie) {
        viewModelScope.launch {
            _isSuccessfullTask.value = saveFavMoviesUseCase(favMovie)
        }
    }

    fun deleteFavMovie(userId: String, movieId: Long) {
        viewModelScope.launch {
            _isSuccessfullTask.value = deleteFavUseCase(userId, movieId)
        }
    }
}
