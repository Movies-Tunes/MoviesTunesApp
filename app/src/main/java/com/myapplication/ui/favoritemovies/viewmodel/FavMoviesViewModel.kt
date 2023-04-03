package com.myapplication.ui.favoritemovies.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.CollectionReference
import com.myapplication.R
import com.myapplication.core.Response
import com.myapplication.core.config.firebase.FAVORITE_FILMS_COLLECTION_FIELD
import com.myapplication.data.model.FavMovie
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FavMoviesViewModel(
    private val collection: CollectionReference,
) : ViewModel() {

    private val _favMovies: MutableLiveData<Response<List<FavMovie?>>> = MutableLiveData()
    val favMovies: LiveData<Response<List<FavMovie?>>> = _favMovies

    suspend fun isFavMovie(userId: String, movieId: Long): Boolean {
        return collection.whereEqualTo("id", movieId)
            .whereEqualTo(
                FAVORITE_FILMS_COLLECTION_FIELD,
                userId,
            ).get().await().isEmpty
    }

    fun getFavMovies(userId: String) {
        viewModelScope.launch {
            _favMovies.value = Response.Loading()
            collection
                .whereEqualTo(FAVORITE_FILMS_COLLECTION_FIELD, userId)
                .get().addOnSuccessListener { task ->
                    val favMovies = task.toObjects(FavMovie::class.java)
                    Log.e("data", favMovies.toString())
                    _favMovies.value = Response.Success(favMovies, R.string.message_add_favorite_movie)
                }.addOnFailureListener {
                    _favMovies.value = Response.Error(it)
                }
        }
    }

    fun saveFavMovie(favMovie: FavMovie) {
        viewModelScope.launch {
            _favMovies.value = Response.Loading()
            collection.add(favMovie).addOnCompleteListener {
                _favMovies.value = Response.Success(listOf())
            }.addOnFailureListener {
                _favMovies.value = Response.Error(it)
            }
        }
    }

    fun deleteFavMovie(userId: String, movieId: Long) {
        viewModelScope.launch {
            _favMovies.value = Response.Loading()
            collection.whereEqualTo("id", movieId)
                .whereEqualTo(FAVORITE_FILMS_COLLECTION_FIELD, userId)
                .get()
                .addOnSuccessListener { task ->
                    task.documents.forEach {
                        collection.document(it.id).delete()
                    }
                    _favMovies.value = Response.Success(listOf(),R.string.message_add_favorite_movie)
                }.addOnFailureListener {
                    _favMovies.value = Response.Error(it)
                }
        }
    }
}
