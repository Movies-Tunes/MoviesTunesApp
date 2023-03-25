package com.myapplication.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "top_rated_tb")
data class TopRatedResultItem(
    @PrimaryKey
    val id: Int,
    @Json(name = "poster_path")
    @ColumnInfo("poster_path")
    val posterPath: String,
    val title: String,
)
