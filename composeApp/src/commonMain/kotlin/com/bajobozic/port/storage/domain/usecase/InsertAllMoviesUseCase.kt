package com.bajobozic.port.storage.domain.usecase

import com.bajobozic.port.network.domain.model.MovieDetail
import com.bajobozic.port.storage.domain.model.Genre

fun interface InsertAllMoviesUseCase :
    suspend (List<MovieDetail>, List<Genre>, List<List<Genre>>) -> Unit