package com.bajobozic.network.domain.usecase

import com.bajobozic.core_component.BaseError
import com.bajobozic.core_component.Outcome
import com.bajobozic.network.domain.model.MovieDetail


fun interface GetMovieDetailUseCase :
    suspend (Int, String) -> Outcome<MovieDetail, BaseError>