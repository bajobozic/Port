package com.bajobozic.storage.domain.usecase

import com.bajobozic.storage.domain.model.TvShowRemoteKeysModel

fun interface GetRemoteKeysByTvShowIdUseCase : suspend (Int) -> TvShowRemoteKeysModel
