package com.bajobozic.movies_component.domain.repository

import androidx.paging.PagingData
import com.bajobozic.storage.domain.model.Movie
import kotlinx.coroutines.flow.Flow

internal interface MoviesRepository {
    fun getPagingData(language: String): Flow<PagingData<Movie>>
}
