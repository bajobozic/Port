package com.bajobozic.port.storage.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bajobozic.port.storage.domain.model.MovieRemoteKeysModel

@Entity(tableName = "movie_remote_keys")
data class MovieRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val movieId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)

fun MovieRemoteKeys.toModel() = MovieRemoteKeysModel(
    movieId = movieId,
    prevKey = prevKey,
    nextKey = nextKey
)

