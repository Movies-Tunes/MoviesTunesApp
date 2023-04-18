package com.myapplication.data.mappers

import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.domain.model.TopRatedMovie

fun TopRatedResultItem.mapToTopRated() =
    TopRatedMovie(
        this.id,
        this.posterPath,
        this.title,
    )
