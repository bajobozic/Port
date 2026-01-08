package com.bajobozic.storage.domain.usecase

import com.bajobozic.storage.domain.model.Genre


fun interface GetAllGenresUseCase : suspend () -> List<Genre>