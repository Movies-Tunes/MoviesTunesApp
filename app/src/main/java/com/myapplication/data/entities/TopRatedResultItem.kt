package com.myapplication.data.entities

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Entity(tableName = "top_rated_tb")
@Parcelize
@Keep
data class TopRatedResultItem(
    @PrimaryKey
    val id: Int,
    @Json(name = "poster_path")
    @ColumnInfo("poster_path")
    val posterPath: String,
    val title: String,
) : Parcelable
