package com.bajobozic.network.domain.usecase

import com.bajobozic.core_component.BaseError
import com.bajobozic.core_component.Outcome
import com.bajobozic.network.domain.model.Genre

fun interface GetGenresUseCase : suspend (String) -> Outcome<List<Genre>, BaseError>