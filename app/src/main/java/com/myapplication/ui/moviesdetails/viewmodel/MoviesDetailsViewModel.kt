package com.myapplication.ui.moviesdetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapplication.core.Response
import com.myapplication.data.entities.MovieDetail
import com.myapplication.domain.usecases.moviedetails.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
) : ViewModel() {

    private val _moviesDetails: MutableLiveData<Response<MovieDetail>> =
        MutableLiveData()
    val moviesDetails: LiveData<Response<MovieDetail>> get() = _moviesDetails
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getMovieDetails(movieId: Long) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            getMovieDetailsUseCase(movieId).let {
                _moviesDetails.postValue(
                    it,
                )
            }
            _isLoading.postValue(false)
        }
    }
}
