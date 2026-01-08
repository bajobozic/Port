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

@Database(
    entities = [MovieEntity::class, GenreEntity::class, MovieGenreCrossRef::class, MovieRemoteKeys::class],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
    abstract fun getMovieRemoteKeysDao(): MovieRemoteKeysDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}