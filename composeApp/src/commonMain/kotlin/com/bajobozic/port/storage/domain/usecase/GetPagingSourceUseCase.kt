package com.bajobozic.port.storage.domain.usecase

import com.bajobozic.port.storage.domain.model.GetMoviesWithGenres

fun interface GetPagingSourceUseCase : () -> androidx.paging.PagingSource<Int, GetMoviesWithGenres>