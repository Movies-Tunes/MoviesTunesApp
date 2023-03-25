package com.myapplication.util

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.myapplication.data.entities.GenreItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.json.JSONObject
import java.lang.reflect.Type

@ProvidedTypeConverter
class TypeConverter {

    @TypeConverter
    fun toGenreItem(json: String): GenreItem {
        val jsonObject = JSONObject(json)
        val id = jsonObject.getInt("id")
        val name = jsonObject.getString("name")
        return GenreItem(id, name)
    }

    @TypeConverter
    fun fromGenreItem(source: GenreItem): JSONObject {
        return JSONObject().apply {
            put("id", source.id)
            put("name", source.name)
        }
    }

    @TypeConverter
    fun toList(json: String): List<GenreItem> {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(
            List::class.java,
            GenreItem::class.java,
        )
        val adapter = moshi.adapter<List<GenreItem>>(type)
        return adapter.fromJson(json)?.map { it } ?: listOf()
    }

    @TypeConverter
    fun fromList(source: List<GenreItem>): String {
        val moshi = Moshi.Builder().build()
        val type =
            Types.newParameterizedType(List::class.java, GenreItem::class.java)
        val adapter = moshi.adapter<List<GenreItem>>(type)
        return adapter.toJson(source) ?: "[]"
    }
}
