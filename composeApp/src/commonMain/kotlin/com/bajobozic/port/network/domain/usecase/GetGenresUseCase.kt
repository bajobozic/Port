package com.bajobozic.port.network.domain.usecase

import com.bajobozic.port.network.domain.model.Genre
import com.bajobozic.port.shared_component.domain.BaseError
import com.bajobozic.port.shared_component.domain.Outcome

fun interface GetGenresUseCase : suspend (String) -> Outcome<List<Genre>, BaseError>