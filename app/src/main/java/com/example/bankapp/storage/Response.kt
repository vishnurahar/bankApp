package com.example.bankapp.storage

sealed class Response<T>(val data : T? = null, val errorMessage: String = ""){
    class Loading<T>() : Response<T>()
    class Success<T>(data: T? = null) : Response<T>(data = data)
    class Error<T>(errorMessage: String) : Response<T>(errorMessage = errorMessage)
}