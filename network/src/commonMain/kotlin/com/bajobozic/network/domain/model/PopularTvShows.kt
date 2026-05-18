package com.bajobozic.network.domain.model

data class PopularTvShows(
    val page: Int,
    val tvShows: List<TvShowDetail> = emptyList(),
    val totalPages: Int,
    val totalResults: Int
)

fun PopularTvShows.initRemoteKeys() =
    this.apply { tvShows.forEach { tvShow -> tvShow.initKeys(this.page) } }
