package sheridan.eajdani.myapplication.Database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken

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
    @TypeConverter
    fun fromJson(json: String?): List<Task>? {
        if (json == null) return null
        val type = object : TypeToken<List<Task>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(list: List<Task>?): String? {
        return Gson().toJson(list)
    }

}