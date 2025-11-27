package com.bajobozic.port.storage.data.util

import androidx.sqlite.SQLiteException
import com.bajobozic.port.shared_component.domain.BaseError
import com.bajobozic.port.shared_component.domain.ErrorHandler
import kotlin.coroutines.cancellation.CancellationException

class DatabaseErrorHandler : ErrorHandler {
    override fun handleError(throwable: Throwable): BaseError {
        return when (throwable) {
            is CancellationException -> throw throwable
            is IllegalStateException -> BaseError.FileError.IllegalStateException
            is SQLiteException -> BaseError.FileError.SQLiteException
            is IllegalArgumentException -> BaseError.FileError.IllegalArgumentException
            is RuntimeException -> BaseError.FileError.RuntimeException
            else -> BaseError.ApiError.UnknownError(throwable.message.orEmpty())
        }
    }
}