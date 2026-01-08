package com.bajobozic.storage.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val id: Int,
    val adult: Boolean,
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String,
    @ColumnInfo(name = "original_language")
    val originalLanguage: String,
    @ColumnInfo(name = "original_title", defaultValue = "")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @ColumnInfo(name = "poster_path", defaultValue = "")
    val posterPath: String,
    @ColumnInfo(name = "release_date")
    val releaseDate: LocalDate,
    val title: String,
    val video: Boolean,
    @ColumnInfo(name = "vote_average", defaultValue = "0.0")
    val voteAverage: Double,
    @ColumnInfo(name = "vote_count", defaultValue = "0")
    val voteCount: Int,
    @ColumnInfo(name = "current_page")
    val currentPage: Int?,
)
