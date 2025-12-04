package com.bajobozic.port.storage.domain.usecase

import com.bajobozic.port.storage.domain.model.MovieRemoteKeysModel

fun interface RemoteKeysInsertAllUseCase : suspend (List<MovieRemoteKeysModel>) -> Unit