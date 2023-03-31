package com.myapplication.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("top_rated_result_tb")
data class TopRatedResult(
    @PrimaryKey
    val page: Int,
    val results: List<TopRatedResultItem>
)
