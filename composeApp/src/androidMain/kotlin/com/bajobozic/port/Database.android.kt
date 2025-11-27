package com.bajobozic.port

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bajobozic.port.storage.data.db.AppDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("my_room.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}