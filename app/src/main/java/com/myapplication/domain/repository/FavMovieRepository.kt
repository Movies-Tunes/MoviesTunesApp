package com.myapplication.domain.repository

import com.google.firebase.firestore.CollectionReference
import com.myapplication.R
import com.myapplication.core.Response
import com.myapplication.core.config.firebase.FAVORITE_FILMS_COLLECTION_FIELD
import com.myapplication.data.model.FavMovie
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface FavMovieRepository {
    suspend fun getFavMovies(userId: String): Response<List<FavMovie>>
    suspend fun isFavMovie(userId: String, movieId: Long): Response<Boolean>
    suspend fun saveMovies(favMovie: FavMovie): Response<Boolean>
    suspend fun deleteFavMovie(userId: String, movieId: Long): Response<Boolean>
}

class FavMovieDatasource @Inject constructor(private val collection: CollectionReference) :
    FavMovieRepository {
    override suspend fun getFavMovies(userId: String): Response<List<FavMovie>> =
        try {
            Response.Success(
                collection
                    .whereEqualTo(FAVORITE_FILMS_COLLECTION_FIELD, userId)
                    .get().await().toObjects(FavMovie::class.java),
            )
        } catch (e: Exception) {
            Response.Error(exception = e)
        }

    override suspend fun isFavMovie(userId: String, movieId: Long): Response<Boolean> =
        try {
            Response.Success(
                collection.whereEqualTo("id", movieId)
                    .whereEqualTo(
                        FAVORITE_FILMS_COLLECTION_FIELD,
                        userId,
                    ).get().await().isEmpty,
            )
        } catch (e: Exception) {
            Response.Error(e)
        }

    override suspend fun saveMovies(favMovie: FavMovie): Response<Boolean> =
        try {
            Response.Success(
                collection.add(favMovie).isSuccessful,
                R.string.message_add_favorite_movie,
            )
        } catch (e: Exception) {
            Response.Error(e)
        }

    override suspend fun deleteFavMovie(userId: String, movieId: Long): Response<Boolean> =
        try {
            mapDeleteFav(movieId, userId)
            Response.Success(true, R.string.message_remove_favorite_movie)
        } catch (e: Exception) {
            Response.Error(e)
        }

    private fun mapDeleteFav(movieId: Long, userId: String) {
        collection.whereEqualTo("id", movieId)
            .whereEqualTo(FAVORITE_FILMS_COLLECTION_FIELD, userId)
            .get().addOnSuccessListener { task ->
                task.documents.forEach {
                    collection.document(it.id).delete()
                }
            }.addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }
}
