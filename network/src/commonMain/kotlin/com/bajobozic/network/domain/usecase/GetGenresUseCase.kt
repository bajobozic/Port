package com.bajobozic.network.domain.usecase

import com.bajobozic.network.domain.model.Genre
import com.bajobozic.shared_component.BaseError
import com.bajobozic.shared_component.Outcome

fun interface GetGenresUseCase : suspend (String) -> Outcome<List<Genre>, BaseError>