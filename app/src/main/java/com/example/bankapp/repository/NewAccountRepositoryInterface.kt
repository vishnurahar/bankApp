package com.example.bankapp.repository

import com.example.bankapp.model.tables.Account
import com.example.bankapp.model.tables.Current_Account
import com.example.bankapp.model.tables.Saving_Account
import com.example.bankapp.model.tables.Transaction
import com.example.bankapp.storage.Response

interface NewAccountRepositoryInterface {
    fun openNewAccount(account: Account) : Response<Unit>

    fun openNewSavingsAccount(savingAccount: Saving_Account) : Response<Unit>

    fun openNewCurrentAccount(currentAccount: Current_Account) : Response<Unit>

    fun getAccount(accountNumber: Long) : Response<Account>

    fun addTransaction(transaction: Transaction) : Response<Unit>
}