package com.bajobozic.storage.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bajobozic.storage.domain.model.GetTvShow
import com.bajobozic.storage.domain.model.TvShow
import kotlinx.datetime.LocalDate

@Entity(tableName = "tv_shows")
internal data class TvShowEntity(
    @PrimaryKey
    @ColumnInfo(name = "tv_show_id")
    val id: Int,
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String,
    @ColumnInfo(name = "original_language")
    val originalLanguage: String,
    @ColumnInfo(name = "original_name", defaultValue = "")
    val originalName: String,
    val overview: String,
    val popularity: Double,
    @ColumnInfo(name = "poster_path", defaultValue = "")
    val posterPath: String,
    @ColumnInfo(name = "first_air_date")
    val firstAirDate: LocalDate,
    val name: String,
    @ColumnInfo(name = "vote_average", defaultValue = "0.0")
    val voteAverage: Double,
    @ColumnInfo(name = "vote_count", defaultValue = "0")
    val voteCount: Int,
    @ColumnInfo(name = "current_page")
    val currentPage: Int?,
) : GetTvShow {

    override fun toModel(): TvShow {
        return TvShow(
            id = id,
            backdropPath = backdropPath,
            originalLanguage = originalLanguage,
            originalName = originalName,
            overview = overview,
            popularity = popularity,
            posterPath = posterPath,
            firstAirDate = firstAirDate,
            name = name,
            voteAverage = voteAverage,
            voteCount = voteCount,
            currentPage = currentPage ?: 0
        )
    }
}


