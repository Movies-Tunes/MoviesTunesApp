package com.myapplication.domain.usecases.favmovies

import com.myapplication.core.Response
import com.myapplication.domain.repository.FavMovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IsFavMovieUseCase {
    suspend operator fun invoke(userId: String, movieId: Long): Response<Boolean>
}

class IsFavMovieUseCaseImpl @Inject constructor(
    private val favMovieRepository: FavMovieRepository,
    private val dispatcher: CoroutineDispatcher,
) : IsFavMovieUseCase {
    override suspend fun invoke(userId: String, movieId: Long): Response<Boolean> =
        withContext(dispatcher) {
            favMovieRepository.isFavMovie(userId, movieId)
        }
}
