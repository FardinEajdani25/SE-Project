package sheridan.eajdani.myapplication.Database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        return value?.let {
            it.split(",").map { item -> item.trim() }
        }
    }

    @TypeConverter
    fun toStringList(value: List<String>?): String? {
        return value?.joinToString(",")
    }
}