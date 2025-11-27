package com.bajobozic.port.shared_component.domain

sealed interface Outcome<out D, out E : BaseError> {
    data class Success<D>(val data: D) : Outcome<D, Nothing>
    data class Error<E : BaseError>(val error: E) : Outcome<Nothing, E>
}


/**
 * Performs the given [action] if this is a [Outcome.Success].
 * Returns the original [Outcome] untouched.
 */
inline fun <D, E : BaseError> Outcome<D, E>.onSuccess(action: (D) -> Unit): Outcome<D, E> {
    if (this is Outcome.Success) {
        action(data)
    }
    return this
}

/**
 * Performs the given [action] if this is a [Outcome.Error].
 * Returns the original [Outcome] untouched.
 */
inline fun <D, E : BaseError> Outcome<D, E>.onError(action: (E) -> Unit): Outcome<D, E> {
    if (this is Outcome.Error) {
        action(error)
    }
    return this
}