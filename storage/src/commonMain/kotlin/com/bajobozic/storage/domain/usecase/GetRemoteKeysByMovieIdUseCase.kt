package com.bajobozic.storage.domain.usecase

import com.bajobozic.storage.domain.model.MovieRemoteKeysModel


fun interface GetRemoteKeysByMovieIdUseCase :
    suspend (Int) -> MovieRemoteKeysModel