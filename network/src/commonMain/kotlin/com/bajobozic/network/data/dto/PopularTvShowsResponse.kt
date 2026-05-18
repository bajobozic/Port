package com.bajobozic.network.data.dto

import com.bajobozic.network.domain.model.PopularTvShows
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PopularTvShowsResponse(
    val page: Int,
    @SerialName(value = "results")
    val tvShows: List<TvShowsResponse> = emptyList(),
    @SerialName(value = "total_pages")
    val totalPages: Int,
    @SerialName(value = "total_results")
    val totalResults: Int
)

internal fun PopularTvShowsResponse.initRemoteKeys() =
    this.apply { tvShows.forEach { tvShow -> tvShow.initKeys(this.page) } }

internal fun PopularTvShowsResponse.toModel() =
    PopularTvShows(
        page = page,
        tvShows = tvShows.map { it.toTvShowDetail() },
        totalPages = totalPages,
        totalResults = totalResults
    )
