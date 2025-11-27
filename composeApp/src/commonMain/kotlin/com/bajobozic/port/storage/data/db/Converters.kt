package com.bajobozic.port.storage.data.db

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class Converters {
    @TypeConverter
    fun fromString(date: String): LocalDate {
        return LocalDate.parse(date)
    }

    @TypeConverter
    fun fromDate(date: LocalDate): String {
        return date.toString()
    }
}