package com.bajobozic.storage.domain.model

data class TvShowRemoteKeysModel(
    val tvShowId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
