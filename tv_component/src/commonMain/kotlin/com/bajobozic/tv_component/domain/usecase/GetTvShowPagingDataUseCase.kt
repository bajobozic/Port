package com.bajobozic.tv_component.domain.usecase

import androidx.paging.PagingData
import com.bajobozic.storage.domain.model.TvShow
import kotlinx.coroutines.flow.Flow

fun interface GetTvShowPagingDataUseCase : (String) -> Flow<PagingData<TvShow>>
