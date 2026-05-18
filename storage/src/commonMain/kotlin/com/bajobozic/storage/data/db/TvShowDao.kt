package com.bajobozic.storage.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bajobozic.storage.data.entity.TvShowEntity

@Dao
internal interface TvShowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvShows(tvShows: List<TvShowEntity>)

    @Query("SELECT * FROM tv_shows ORDER BY popularity DESC")
    fun pagingSource(): PagingSource<Int, TvShowEntity>

    @Query("DELETE FROM tv_shows")
    suspend fun clearAll()

    @Query("SELECT MAX(current_page) FROM tv_shows")
    suspend fun getMaxCurrentPage(): Int?
}
