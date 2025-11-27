package com.bajobozic.port.storage.domain.usecase

fun interface BatchTransactionUseCase : suspend (suspend () -> Unit) -> Unit