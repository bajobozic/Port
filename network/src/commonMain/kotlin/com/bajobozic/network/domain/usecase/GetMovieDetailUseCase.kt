package com.bajobozic.network.domain.usecase

import com.bajobozic.network.domain.model.MovieDetail
import com.bajobozic.shared_component.BaseError
import com.bajobozic.shared_component.Outcome


fun interface GetMovieDetailUseCase :
    suspend (Int, String) -> Outcome<MovieDetail, BaseError>