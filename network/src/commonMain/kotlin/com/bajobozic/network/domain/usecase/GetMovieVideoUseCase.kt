package com.bajobozic.network.domain.usecase

import com.bajobozic.core_component.BaseError
import com.bajobozic.core_component.Outcome
import com.bajobozic.network.domain.model.MovieVideo


fun interface GetMovieVideoUseCase :
    suspend (String, Int) -> Outcome<List<MovieVideo>, BaseError>