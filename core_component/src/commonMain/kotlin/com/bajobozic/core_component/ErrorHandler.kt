package com.bajobozic.core_component

interface ErrorHandler {
    fun handleError(throwable: Throwable): BaseError
}