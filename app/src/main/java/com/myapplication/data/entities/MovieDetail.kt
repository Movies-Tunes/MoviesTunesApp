package com.myapplication.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity("movie_detail_tb")
data class MovieDetail(
    @PrimaryKey
    val id: Int,
    val overview: String,
    @Json(name = "poster_path")
    val posterPath: String,
    @Json(name = "release_date")
    val releaseDate: String,
    val runtime: Int,
    val genres: List<GenreItem>
)
