package com.myapplication.data.entities

import com.squareup.moshi.Json

data class MovieDetail(
    val overview: String,
    @Json(name = "poster_path")
    val posterPath: String,
    @Json(name = "release_date")
    val releaseDate: String,
    val runtime: Int,
    val genres: List<GenreItem>
)
