package com.bajobozic.port.storage.domain.model

data class MovieRemoteKeysModel(
    val movieId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)