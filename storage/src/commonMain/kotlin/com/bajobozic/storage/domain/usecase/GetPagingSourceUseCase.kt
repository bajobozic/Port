package com.bajobozic.storage.domain.usecase

import androidx.paging.PagingSource
import com.bajobozic.storage.domain.model.GetMovieWithGenres


fun interface GetPagingSourceUseCase : () -> PagingSource<Int, GetMovieWithGenres>