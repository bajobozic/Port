package com.bajobozic.port.home.data.remote

import com.bajobozic.port.shared_component.domain.BaseError
import com.bajobozic.port.shared_component.domain.ErrorHandler
import kotlin.coroutines.cancellation.CancellationException

class GlobalErrorHandler : ErrorHandler {
    override fun handleError(throwable: Throwable): BaseError {
        return when (throwable) {
            is CancellationException -> throw throwable
//            is UnknownHostException -> BaseError.ApiError.NoInternet
//            is IOException -> BaseError.ApiError.NoInternet
//            is HttpException -> BaseError.ApiError.ToManyRequest
            else -> BaseError.ApiError.UnknownError(throwable.message.orEmpty())
        }
    }
}