package com.example.auraprototype.model

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message :String, val cause: Throwable? = null) : Resource<T>()

    class Loading<T> : Resource<T>()
}