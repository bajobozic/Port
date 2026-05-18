package com.bajobozic.network.domain.usecase

import com.bajobozic.network.domain.model.PopularTvShows
import com.bajobozic.shared_component.BaseError
import com.bajobozic.shared_component.Outcome

fun interface GetTvShowsUseCase :
    suspend (String, Int) -> Outcome<PopularTvShows, BaseError>
