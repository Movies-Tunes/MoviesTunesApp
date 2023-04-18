package com.myapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopRatedMovie(
    val id: Int,
    val posterPath: String,
    val title: String,
) : Parcelable
