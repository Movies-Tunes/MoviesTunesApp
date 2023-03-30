package com.myapplication.core

sealed class Response<T> {
    class Loading<T> : Response<T>()
    data class Error<T>(val exception: Exception) : Response<T>()
    data class Success<T>(val data: T) : Response<T>()
}
