package com.myapplication.util.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.myapplication.data.entities.GenreItem
import com.myapplication.data.entities.TopRatedResultItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type: Type = Types.newParameterizedType(
            List::class.java,
            GenreItem::class.java,
        )
        val adapter = moshi.adapter<List<GenreItem>>(type)
        return adapter.fromJson(json)?.map { it } ?: listOf()
    }

    @TypeConverter
    fun fromList(source: List<GenreItem>): String {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type =
            Types.newParameterizedType(List::class.java, GenreItem::class.java)
        val adapter = moshi.adapter<List<GenreItem>>(type)
        return adapter.toJson(source) ?: "[]"
    }

    @TypeConverter
    fun fromListTopRated(movies: List<TopRatedResultItem>): String {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type =
            Types.newParameterizedType(List::class.java, TopRatedResultItem::class.java)
        val adapter = moshi.adapter<List<TopRatedResultItem>>(type)
        return adapter.toJson(movies) ?: "[]"
    }

    @TypeConverter
    fun toListTopRated(json: String): List<TopRatedResultItem> {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type =
            Types.newParameterizedType(List::class.java, TopRatedResultItem::class.java)
        val adapter = moshi.adapter<List<TopRatedResultItem>>(type)
        return adapter.fromJson(json) ?: listOf()
    }
}
