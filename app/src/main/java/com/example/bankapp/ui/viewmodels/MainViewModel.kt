package com.example.bankapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bankapp.model.tables.Account
import com.example.bankapp.model.tables.User
import com.example.bankapp.repository.AccountRepository
import com.example.bankapp.repository.AuthRepository
import com.example.bankapp.repository.UserRepository
import com.example.bankapp.storage.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val userRepository: UserRepository = UserRepository(application)
    private val accountRepository: AccountRepository = AccountRepository(application)

    private val loggedInUser = MutableLiveData<Response<User?>>()
    val currentUser: LiveData<Response<User?>>
        get() = loggedInUser

    private val _userAccounts = MutableLiveData<Response<List<Account>?>>()
    val userAccounts: LiveData<Response<List<Account>?>>
        get() = _userAccounts

    fun getUserAccounts(customerId: Int){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                accountRepository.getUserAccounts(customerId)
            }
            _userAccounts.postValue(result)
        }
    }

    fun getLoggedInUser(email: String?){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                userRepository.getCurrentUser(email)
            }
            loggedInUser.postValue(result)
        }
    }

}