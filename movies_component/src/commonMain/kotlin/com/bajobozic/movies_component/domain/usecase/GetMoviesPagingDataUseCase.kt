package com.bajobozic.movies_component.domain.usecase

import androidx.paging.PagingData
import com.bajobozic.storage.domain.model.Movie
import kotlinx.coroutines.flow.Flow

fun interface GetMoviesPagingDataUseCase : (String) -> Flow<PagingData<Movie>>
