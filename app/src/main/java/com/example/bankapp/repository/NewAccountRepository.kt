package com.example.bankapp.repository

import android.app.Application
import com.example.bankapp.model.tables.*
import com.example.bankapp.storage.BankAppDatabase
import com.example.bankapp.storage.Response
import com.example.bankapp.storage.dao.AccountDao
import com.example.bankapp.storage.dao.CurrentAccountDao
import com.example.bankapp.storage.dao.SavingAccountDao
import com.example.bankapp.storage.dao.TransactionDao

class NewAccountRepository (application: Application) : NewAccountRepositoryInterface{

    private val accountDao: AccountDao = BankAppDatabase.getDatabase(application).accountsDao()
    private val savingAccountDao: SavingAccountDao = BankAppDatabase.getDatabase(application).savingAccountDao()
    private val currentAccountDao: CurrentAccountDao = BankAppDatabase.getDatabase(application).currentAccountDao()
    private val transactionDao: TransactionDao = BankAppDatabase.getDatabase(application).transactionDao()

    override fun openNewAccount(account: Account) : Response<Unit> {
        return try {
            accountDao.insert(account)
            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    override fun openNewSavingsAccount(savingAccount: Saving_Account): Response<Unit> {
        return try {
            savingAccountDao.insert(savingAccount)
            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    override fun openNewCurrentAccount(currentAccount: Current_Account): Response<Unit> {
        return try {
            currentAccountDao.insert(currentAccount)
            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    override fun getAccount(accountNumber: Long): Response<Account> {
        return try {
            Response.Success(accountDao.getAccount(accountNumber))
        }catch (e:Exception){
            Response.Error(e.message.toString())
        }
    }

    override fun addTransaction(transaction: Transaction): Response<Unit> {
        return try {
            Response.Success(transactionDao.insertTransaction(transaction))
        }catch (e:Exception){
            Response.Error(e.message.toString())
        }
    }
}