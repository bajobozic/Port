package com.bajobozic.storage.domain.usecase

import com.bajobozic.storage.domain.model.TvShowDetail

fun interface DeleteThenInsertAllTvShowsUseCase : suspend (List<TvShowDetail>) -> Unit
