package com.bajobozic.tv_component.domain.repository

import androidx.paging.PagingData
import com.bajobozic.storage.domain.model.TvShow
import kotlinx.coroutines.flow.Flow

internal interface TvRepository {
    fun getPagingData(language: String): Flow<PagingData<TvShow>>
}
