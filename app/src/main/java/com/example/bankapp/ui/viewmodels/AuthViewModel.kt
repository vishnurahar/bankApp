package com.example.bankapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bankapp.model.tables.User
import com.example.bankapp.repository.AuthRepository
import com.example.bankapp.storage.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository: AuthRepository = AuthRepository(application)

    private val _loginResult = MutableLiveData<Response<User>>()
    val loginResult: LiveData<Response<User>>
        get() = _loginResult

    private val _signupResult = MutableLiveData<Response<Unit>>()
    val signupResult: LiveData<Response<Unit>>
        get() = _signupResult

    internal fun checkValidLogin(email: String, password: String): Boolean {
        return authRepository.isValidAccount(email, password)
    }


    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                authRepository.login(email, password)
            }
            _loginResult.postValue(result)
        }
    }

    fun signup(name: String, email: String, password: String, phone: Long, address: String, age: Int) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                authRepository.signUp(name, email, password, phone, address, age)
            }
            _signupResult.postValue(result)
        }
    }
}