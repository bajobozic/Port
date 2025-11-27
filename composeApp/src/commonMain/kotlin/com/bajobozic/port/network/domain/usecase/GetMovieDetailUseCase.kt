package com.bajobozic.port.network.domain.usecase

import com.bajobozic.port.network.domain.model.MovieDetail
import com.bajobozic.port.shared_component.domain.BaseError
import com.bajobozic.port.shared_component.domain.Outcome

fun interface GetMovieDetailUseCase :
    suspend (Int, String) -> Outcome<MovieDetail, BaseError>