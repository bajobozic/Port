package com.bajobozic.port.storage.domain.usecase

import com.bajobozic.port.storage.domain.model.Genre

fun interface GetAllGenresUseCase : suspend () -> List<Genre>