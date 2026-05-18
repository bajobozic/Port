package com.bajobozic.storage.domain.usecase

import androidx.paging.PagingSource
import com.bajobozic.storage.domain.model.GetTvShow

fun interface GetTvShowPagingSourceUseCase : () -> PagingSource<Int, GetTvShow>
