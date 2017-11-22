package locavoreplatine.locafarm.database

import android.arch.persistence.room.TypeConverter

import java.util.Date


class Converters {


    @TypeConverter
    fun toDate(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

}