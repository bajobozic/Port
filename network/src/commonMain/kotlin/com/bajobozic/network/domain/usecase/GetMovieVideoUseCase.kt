package com.bajobozic.network.domain.usecase

import com.bajobozic.network.domain.model.MovieVideo
import com.bajobozic.shared_component.BaseError
import com.bajobozic.shared_component.Outcome


fun interface GetMovieVideoUseCase :
    suspend (String, Int) -> Outcome<List<MovieVideo>, BaseError>