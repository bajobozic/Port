package com.bajobozic.storage.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bajobozic.storage.domain.model.TvShowRemoteKeysModel

@Entity(tableName = "tv_show_remote_keys")
internal data class TvShowRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val tvShowId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)

internal fun TvShowRemoteKeys.toModel() = TvShowRemoteKeysModel(
    tvShowId = tvShowId,
    prevKey = prevKey,
    nextKey = nextKey
)
