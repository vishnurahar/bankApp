package com.example.bankapp.repository

import android.database.Observable
import com.example.bankapp.model.tables.User
import com.example.bankapp.storage.Response

interface AuthRepositoryInterface  {
//    fun signIn(): Observable<FetchEvent<SignInPostResponse>>
    fun login(email: String, password: String): Response<User>

    fun getLoggedInUser(email: String): Response<User>

//    fun signUp(username: String, email: String, password: String): Observable<FetchEvent<SignUpPostResponse>>
    fun signUp(name: String, email: String, password: String, phone: Long, address: String, age: Int): Response<Unit>
}