package com.myapplication.data.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavMovie(
    @get: PropertyName("id")
    @set: PropertyName("id")
    var id: Long = 0,
    @get: PropertyName("posterPath")
    @set: PropertyName("posterPath")
    var posterPath: String = "",
    @get: PropertyName("title")
    @set: PropertyName("title")
    var title: String = "",
    @get: PropertyName("userId")
    @set: PropertyName("userId")
    var userId: String = "",
) : Parcelable
