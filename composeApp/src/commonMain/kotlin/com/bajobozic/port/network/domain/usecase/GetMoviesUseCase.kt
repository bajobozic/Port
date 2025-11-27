package com.bajobozic.port.network.domain.usecase

import com.bajobozic.port.network.domain.model.PopularMovies
import com.bajobozic.port.shared_component.domain.BaseError
import com.bajobozic.port.shared_component.domain.Outcome

fun interface GetMoviesUseCase :
    suspend (String, Int) -> Outcome<PopularMovies, BaseError>