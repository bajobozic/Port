package com.bajobozic.network.domain.usecase

import com.bajobozic.core_component.BaseError
import com.bajobozic.core_component.Outcome
import com.bajobozic.network.domain.model.PopularMovies

fun interface GetMoviesUseCase :
    suspend (String, Int) -> Outcome<PopularMovies, BaseError>