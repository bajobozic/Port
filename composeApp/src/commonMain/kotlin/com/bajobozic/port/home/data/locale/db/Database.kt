package com.bajobozic.port.home.data.locale.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.bajobozic.port.home.data.locale.entity.GenreEntity
import com.bajobozic.port.home.data.locale.entity.MovieEntity
import com.bajobozic.port.home.data.locale.entity.MovieGenreCrossRef

@Database(
    entities = [MovieEntity::class, GenreEntity::class, MovieGenreCrossRef::class],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}