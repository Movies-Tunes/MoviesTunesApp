package com.myapplication.domain.usecases.moviedetails

import com.myapplication.core.Response
import com.myapplication.data.entities.MovieDetail
import com.myapplication.domain.di.modules.DefaultDispatchers
import com.myapplication.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

interface GetMovieDetailsUseCase {
    suspend operator fun invoke(movieId: Long): Response<MovieDetail>
}

class GetMovieDetailsUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository,
    @DefaultDispatchers private val dispatcher: CoroutineDispatcher,
) : GetMovieDetailsUseCase {
    override suspend fun invoke(movieId: Long): Response<MovieDetail> =
        withContext(dispatcher) {
            movieRepository.getMovieDetails(movieId, Locale.getDefault().toLanguageTag())
        }
}
