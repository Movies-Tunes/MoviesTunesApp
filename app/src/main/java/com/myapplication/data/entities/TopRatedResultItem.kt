package com.myapplication.data.entities

import com.squareup.moshi.Json


data class TopRatedResultItem(
    val id: Int,
    @Json(name = "poster_path") val posterPath: String,
    val title: String
)
