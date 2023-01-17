package com.example.bankapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bankapp.model.tables.Account
import com.example.bankapp.model.tables.Current_Account
import com.example.bankapp.model.tables.Saving_Account
import com.example.bankapp.model.tables.Transaction
import com.example.bankapp.repository.AccountRepository
import com.example.bankapp.storage.BankAppDatabase
import com.example.bankapp.storage.Response
import com.example.bankapp.storage.dao.TransactionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val accountRepository: AccountRepository = AccountRepository(application)

    private val _accountResult = MutableLiveData<Response<Account>>()
    val accountResult: LiveData<Response<Account>>
        get() = _accountResult

    private val _updateAccountResult = MutableLiveData<Response<Unit>>()
    val updateAccountResult: LiveData<Response<Unit>>
        get() = _updateAccountResult

    private val _savingAccountResult = MutableLiveData<Response<Saving_Account>>()
    val savingAccountResult: LiveData<Response<Saving_Account>>
        get() = _savingAccountResult

    private val _currentAccountResult = MutableLiveData<Response<Current_Account>>()
    val currentAccountResult: LiveData<Response<Current_Account>>
        get() = _currentAccountResult

    private val _transactionResult = MutableLiveData<Response<List<Transaction>>>()
    val transactionResult: LiveData<Response<List<Transaction>>>
        get() = _transactionResult

    fun getAccount(accountId: Long) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                accountRepository.getAccount(accountId)
            }
            _accountResult.postValue(result)
        }
    }

    fun updateAccount(account: Account) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                accountRepository.updateAccount(account)
            }
            _updateAccountResult.postValue(result)
        }
    }

    fun getTransactionHistory(accountId: Long) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                accountRepository.getTransactionHistory(accountId)
            }
            _transactionResult.postValue(result)
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                accountRepository.addTransaction(transaction)
            }
        }
    }

    fun getSavingAccount(accountId: Long) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                accountRepository.getSavingAccount(accountId)
            }
            val accResult = withContext(Dispatchers.IO) {
                accountRepository.getAccount(accountId)
            }
            _savingAccountResult.postValue(result)
            _accountResult.postValue(accResult)
        }
    }

    fun getCurrentAccount(accountId: Long) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                accountRepository.getCurrentAccount(accountId)
            }
            val accResult = withContext(Dispatchers.IO) {
                accountRepository.getAccount(accountId)
            }
            _currentAccountResult.postValue(result)
            _accountResult.postValue(accResult)
        }
    }

    private val _destinationAccount = MutableLiveData<Response<Account>>()
    val destinationAccount: LiveData<Response<Account>>
        get() = _destinationAccount

    fun getDestinationAccount(accountId: Long) {
        viewModelScope.launch {
            val account = withContext(Dispatchers.IO){
                accountRepository.getAccount(accountId)
            }
            _destinationAccount.postValue(account)
        }

    }

}