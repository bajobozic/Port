package com.bajobozic.storage.domain.usecase

import com.bajobozic.storage.domain.model.TvShowDetail

fun interface InsertAllTvShowsUseCase : suspend (List<TvShowDetail>) -> Unit
