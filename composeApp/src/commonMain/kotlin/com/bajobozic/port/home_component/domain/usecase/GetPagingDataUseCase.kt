package com.bajobozic.port.home_component.domain.usecase

import androidx.paging.PagingData
import com.bajobozic.port.storage.domain.model.Movie
import kotlinx.coroutines.flow.Flow

fun interface GetPagingDataUseCase : (String) -> Flow<PagingData<Movie>>