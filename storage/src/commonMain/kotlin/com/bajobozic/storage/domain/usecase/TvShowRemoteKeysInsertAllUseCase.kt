package com.bajobozic.storage.domain.usecase

import com.bajobozic.storage.domain.model.TvShowRemoteKeysModel

fun interface TvShowRemoteKeysInsertAllUseCase : suspend (List<TvShowRemoteKeysModel>) -> Unit
