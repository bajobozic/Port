package com.bajobozic.storage.domain.usecase

import com.bajobozic.storage.domain.repository.StorageRepository

fun interface DeleteMovieUseCase : suspend (Int) -> Unit

internal fun DeleteMovieUseCase(repository: StorageRepository) = DeleteMovieUseCase { movieId ->
    repository.deleteMovie(movieId)
}
