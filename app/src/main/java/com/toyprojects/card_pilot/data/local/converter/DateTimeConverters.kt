package com.toyprojects.card_pilot.data.local.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime

class DateTimeConverters {
    @TypeConverter
    fun fromString(value: String?): LocalDateTime? {
        return value?.let {
            LocalDateTime.parse(it)
        }
    }

    @TypeConverter
    fun dateToString(date: LocalDateTime?): String? {
        return date?.toString()
    }
}
