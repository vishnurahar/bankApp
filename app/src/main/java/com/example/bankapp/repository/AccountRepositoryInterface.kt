package com.example.bankapp.repository

import com.example.bankapp.model.tables.Account
import com.example.bankapp.model.tables.Current_Account
import com.example.bankapp.model.tables.Saving_Account
import com.example.bankapp.model.tables.Transaction
import com.example.bankapp.storage.Response

interface AccountRepositoryInterface {

    fun getAccount(account_id: Long): Response<Account>

    fun updateAccount(account: Account): Response<Unit>

    fun checkAccountExistence(account_id: Long): Boolean

    fun getTransactionHistory(account_id: Long): Response<List<Transaction>>

    fun addTransaction(transaction: Transaction): Response<Unit>

    fun getSavingAccount(account_id: Long): Response<Saving_Account>

    fun getCurrentAccount(account_id: Long): Response<Current_Account>

    fun getUserAccounts(customerId: Int) : Response<List<Account>?>
}