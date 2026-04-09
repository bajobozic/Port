package com.bajobozic.storage.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bajobozic.storage.data.entity.MovieRemoteKeys

@Dao
internal interface MovieRemoteKeysDao {

    @Query("SELECT * FROM movie_remote_keys WHERE movieId = :id")
    suspend fun remoteKeysByMovieId(id: Int): MovieRemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<MovieRemoteKeys>)

    @Query("DELETE FROM movie_remote_keys")
    suspend fun clearRemoteKeys()
}
