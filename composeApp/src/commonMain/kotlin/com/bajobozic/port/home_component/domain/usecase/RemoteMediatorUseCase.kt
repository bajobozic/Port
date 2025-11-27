package com.bajobozic.port.home_component.domain.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.bajobozic.port.storage.data.entity.GenreWithMovies

interface RemoteMediatorUseCase {
    @OptIn(ExperimentalPagingApi::class)
    suspend fun getRemoteMediator(): RemoteMediator<Int, GenreWithMovies>
}