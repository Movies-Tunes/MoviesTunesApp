package com.myapplication.domain.usecases.favmovies

import com.myapplication.core.Response
import com.myapplication.data.model.FavMovie
import com.myapplication.domain.repository.FavMovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface SaveFavMoviesUseCase {
    suspend operator fun invoke(favMovie: FavMovie): Response<Boolean>
}

class SaveFavMoviesUseCaseImpl @Inject constructor(
    private val favMovieRepository: FavMovieRepository,
    private val dispatcher: CoroutineDispatcher
): SaveFavMoviesUseCase {
    override suspend fun invoke(favMovie: FavMovie): Response<Boolean> =
        withContext(dispatcher) {
            favMovieRepository.saveMovies(favMovie)
        }

}
