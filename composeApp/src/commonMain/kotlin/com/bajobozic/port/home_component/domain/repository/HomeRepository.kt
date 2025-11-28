package com.bajobozic.port.home_component.domain.repository

import androidx.paging.PagingData
import com.bajobozic.port.storage.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getPagingData(language: String): Flow<PagingData<Movie>>
}