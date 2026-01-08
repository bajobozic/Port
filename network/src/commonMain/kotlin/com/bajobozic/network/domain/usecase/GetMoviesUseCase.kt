package com.bajobozic.network.domain.usecase

import com.bajobozic.network.domain.model.PopularMovies
import com.bajobozic.shared_component.BaseError
import com.bajobozic.shared_component.Outcome

fun interface GetMoviesUseCase :
    suspend (String, Int) -> Outcome<PopularMovies, BaseError>