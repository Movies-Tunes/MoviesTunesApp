package com.myapplication.core

import androidx.annotation.StringRes
import com.myapplication.R

sealed class Response<T> {
    class Loading<T> : Response<T>()
    data class Error<T>(val exception: Exception) : Response<T>()
    data class Success<T>(
        val data: T,
        @StringRes val message: Int = R.string.message_success_default,
    ) : Response<T>()
}
