package com.bajobozic.storage.data.util

import androidx.sqlite.SQLiteException
import com.bajobozic.shared_component.BaseError
import com.bajobozic.shared_component.ErrorHandler
import kotlin.coroutines.cancellation.CancellationException

internal class DatabaseErrorHandler : ErrorHandler {
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