package com.bajobozic.port.home.domain

sealed interface BaseError {
    sealed interface ApiError : BaseError {
        data object NoInternet : ApiError
        data object ToManyRequest : ApiError
        data class UnknownError(val message:String): ApiError
    }

    sealed interface FileError : BaseError {
        data object FileNotFound : ApiError
        data object DatabaseAccessError : ApiError

    }
}

interface ErrorHandler {
    fun handleError(throwable: Throwable): BaseError
}