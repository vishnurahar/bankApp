package com.example.bankapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bankapp.model.tables.User
import com.example.bankapp.repository.UserRepository
import com.example.bankapp.storage.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(application: Application): AndroidViewModel(application) {

    private val userRepository: UserRepository = UserRepository(application)

    private val loggedInUser = MutableLiveData<Response<User?>>()
    val currentUser: LiveData<Response<User?>>
        get() = loggedInUser

    fun getLoggedInUser(customerId: Int){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                userRepository.getCurrentUserById(customerId)
            }
            loggedInUser.postValue(result)
        }
    }
}