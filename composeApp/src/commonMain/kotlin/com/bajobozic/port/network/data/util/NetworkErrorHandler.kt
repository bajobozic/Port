package com.bajobozic.port.network.data.util

import com.bajobozic.port.shared_component.domain.BaseError
import com.bajobozic.port.shared_component.domain.ErrorHandler
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import kotlin.coroutines.cancellation.CancellationException

internal class NetworkErrorHandler : ErrorHandler {
    override fun handleError(throwable: Throwable): BaseError {
        return when (throwable) {
            is CancellationException -> throw throwable
            is ClientRequestException -> BaseError.ApiError.HttpServerError(
                throwable.response.status.value,
                throwable.message
            )

            is ServerResponseException -> BaseError.ApiError.HttpServerError(
                throwable.response.status.value,
                throwable.message
            )

            is kotlinx.io.IOException -> BaseError.ApiError.NoInternet
            is HttpRequestTimeoutException -> BaseError.ApiError.NoInternet
            else -> BaseError.ApiError.UnknownError(throwable.message.orEmpty())
        }
    }
}