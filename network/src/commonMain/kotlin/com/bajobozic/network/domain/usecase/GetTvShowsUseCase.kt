package com.bajobozic.network.domain.usecase

import com.bajobozic.core_component.BaseError
import com.bajobozic.core_component.Outcome
import com.bajobozic.network.domain.model.PopularTvShows

fun interface GetTvShowsUseCase :
    suspend (String, Int) -> Outcome<PopularTvShows, BaseError>
