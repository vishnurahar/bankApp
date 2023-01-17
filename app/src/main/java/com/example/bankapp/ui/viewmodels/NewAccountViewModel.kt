package com.example.bankapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bankapp.model.tables.*
import com.example.bankapp.repository.NewAccountRepository
import com.example.bankapp.repository.UserRepository
import com.example.bankapp.storage.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewAccountViewModel(application: Application): AndroidViewModel(application) {

    private val newAccountRepository: NewAccountRepository = NewAccountRepository(application)
    private val userRepository: UserRepository = UserRepository(application)

    private val _newAccountResult = MutableLiveData<Response<Unit>>()
    val newAccountResult: LiveData<Response<Unit>>
        get() = _newAccountResult

    private val _getAccountResult = MutableLiveData<Response<Account>>()
    val getAccountResult: LiveData<Response<Account>>
        get() = _getAccountResult

    private val _getUserResult = MutableLiveData<Response<User?>>()
    val getUserResult: LiveData<Response<User?>>
        get() = _getUserResult

    private val _newSavingsAccountResult = MutableLiveData<Response<Unit>>()
    val newSavingsAccountResult: LiveData<Response<Unit>>
        get() = _newSavingsAccountResult

    private val _newCurrentAccountResult = MutableLiveData<Response<Unit>>()
    val newCurrentAccountResult: LiveData<Response<Unit>>
        get() = _newCurrentAccountResult

    fun newAccount(account: Account){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                newAccountRepository.openNewAccount(account)
            }
            _newAccountResult.postValue(result)
        }
    }

    fun newSavingsAccount(savingAccount: Saving_Account){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                newAccountRepository.openNewSavingsAccount(savingAccount)
            }
            _newSavingsAccountResult.postValue(result)
        }
    }

    fun newCurrentAccount(currentAccount: Current_Account){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                newAccountRepository.openNewCurrentAccount(currentAccount)
            }
            _newCurrentAccountResult.postValue(result)
        }
    }

    fun getAccount(accountNumber: Long){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                newAccountRepository.getAccount(accountNumber)
            }
            _getAccountResult.postValue(result)
        }
    }

    fun getUser(customerId: Int){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                userRepository.getCurrentUserById(customerId)
            }
            _getUserResult.postValue(result)
        }
    }

    fun addTransaction(transaction: Transaction){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                newAccountRepository.addTransaction(transaction)
            }
        }
    }
}