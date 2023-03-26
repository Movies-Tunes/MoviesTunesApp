package com.myapplication.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("genre_item_tb")
data class GenreItem(
    @PrimaryKey
    val id: Int,
    val name: String
)
