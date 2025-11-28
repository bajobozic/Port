package com.bajobozic.port.storage.domain.usecase

import androidx.paging.PagingSource
import com.bajobozic.port.storage.domain.model.GetMovieWithGenres

fun interface GetPagingSourceUseCase : () -> PagingSource<Int, GetMovieWithGenres>