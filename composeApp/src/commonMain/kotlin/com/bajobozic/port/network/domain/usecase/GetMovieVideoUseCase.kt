package com.bajobozic.port.network.domain.usecase

import com.bajobozic.port.network.domain.model.MovieVideo
import com.bajobozic.port.shared_component.domain.BaseError
import com.bajobozic.port.shared_component.domain.Outcome

fun interface GetMovieVideoUseCase :
    suspend (String, Int) -> Outcome<List<MovieVideo>, BaseError>