package com.bajobozic.port.shared_component.domain

interface ErrorHandler {
    fun handleError(throwable: Throwable): BaseError
}