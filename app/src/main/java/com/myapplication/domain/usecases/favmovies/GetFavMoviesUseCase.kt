package com.myapplication.domain.usecases.favmovies

import com.myapplication.core.Response
import com.myapplication.data.model.FavMovie
import com.myapplication.domain.repository.FavMovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetFavMoviesUseCase {
    suspend operator fun invoke(userId: String): Response<List<FavMovie>>
}

class GetFavMoviesUseCaseImpl @Inject constructor(
    private val favMovieRepository: FavMovieRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : GetFavMoviesUseCase {
    override suspend fun invoke(userId: String): Response<List<FavMovie>> =
        withContext(dispatcher) {
            favMovieRepository.getFavMovies(userId)
        }

}
