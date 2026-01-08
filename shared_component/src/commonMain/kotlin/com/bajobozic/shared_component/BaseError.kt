package com.bajobozic.shared_component

sealed interface BaseError {
    sealed interface ApiError : BaseError {
        data object NoInternet : ApiError
        data object ToManyRequest : ApiError
        data class HttpServerError(val code: Int, val message: String) : ApiError
        data class UnknownError(val message: String) : ApiError
    }

    sealed interface FileError : BaseError {
        data object IllegalStateException : FileError
        data object IllegalArgumentException : FileError
        data object SQLiteException : FileError
        data object RuntimeException : FileError
        data class UnknownError(val message: String) : ApiError
    }
}