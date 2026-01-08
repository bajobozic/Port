package com.bajobozic.shared_component

interface ErrorHandler {
    fun handleError(throwable: Throwable): BaseError
}