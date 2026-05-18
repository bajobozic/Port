package com.bajobozic.storage.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.bajobozic.storage.data.entity.GenreEntity
import com.bajobozic.storage.data.entity.MovieEntity
import com.bajobozic.storage.data.entity.MovieGenreCrossRef
import com.bajobozic.storage.data.entity.MovieRemoteKeys
import com.bajobozic.storage.data.entity.TvShowEntity
import com.bajobozic.storage.data.entity.TvShowRemoteKeys

@Database(
    entities = [MovieEntity::class, GenreEntity::class, MovieGenreCrossRef::class, MovieRemoteKeys::class, TvShowEntity::class, TvShowRemoteKeys::class],
    version = 2
)
@ConstructedBy(AppDatabaseConstructor::class)
@TypeConverters(Converters::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
    abstract fun getMovieRemoteKeysDao(): MovieRemoteKeysDao
    abstract fun getTvShowDao(): TvShowDao
    abstract fun getTvShowRemoteKeysDao(): TvShowRemoteKeysDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
internal expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}