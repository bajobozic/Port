package com.bajobozic.storage.domain.usecase

import com.bajobozic.storage.domain.model.Genre
import com.bajobozic.storage.domain.model.MovieDetail


fun interface InsertAllMoviesUseCase :
    suspend (List<MovieDetail>, List<Genre>, List<List<Genre>>) -> Unit