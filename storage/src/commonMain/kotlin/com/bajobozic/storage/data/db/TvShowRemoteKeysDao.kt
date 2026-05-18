package com.bajobozic.storage.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bajobozic.storage.data.entity.TvShowRemoteKeys

@Dao
internal interface TvShowRemoteKeysDao {

    @Query("SELECT * FROM tv_show_remote_keys WHERE tvShowId = :id")
    suspend fun remoteKeysByTvShowId(id: Int): TvShowRemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<TvShowRemoteKeys>)

    @Query("DELETE FROM tv_show_remote_keys")
    suspend fun clearRemoteKeys()
}
