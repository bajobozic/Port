package com.bajobozic.port.home_component.domain.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.bajobozic.port.storage.data.entity.GenreWithMovies

class RemoteMediatorUseCaseImpl() : RemoteMediatorUseCase {
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getRemoteMediator(): RemoteMediator<Int, GenreWithMovies> {
        TODO("Not yet implemented")
    }
}