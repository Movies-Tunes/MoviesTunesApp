package com.myapplication.ui.movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.CollectionReference
import com.myapplication.data.entities.MovieDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FavoriteFilmsViewModel(
    val collection: CollectionReference,
) : ViewModel() {

    fun insert(movie: MovieDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            collection.add(movie).addOnSuccessListener {
            }.await()
            val get = collection.get().await()
            val toObjects = get.toObjects(MovieDetail::class.java)
        }
    }
}
