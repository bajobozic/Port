package com.bajobozic.storage.domain.usecase

fun interface BatchTransactionUseCase : suspend (suspend () -> Unit) -> Unit