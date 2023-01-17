package com.example.bankapp.repository

import android.app.Application
import com.example.bankapp.model.tables.Account
import com.example.bankapp.model.tables.Current_Account
import com.example.bankapp.model.tables.Saving_Account
import com.example.bankapp.model.tables.Transaction
import com.example.bankapp.storage.BankAppDatabase
import com.example.bankapp.storage.Response
import com.example.bankapp.storage.dao.AccountDao
import com.example.bankapp.storage.dao.CurrentAccountDao
import com.example.bankapp.storage.dao.SavingAccountDao
import com.example.bankapp.storage.dao.TransactionDao

class AccountRepository(application: Application) : AccountRepositoryInterface {

    private val accountDao: AccountDao = BankAppDatabase.getDatabase(application).accountsDao()
    private val savingAccountDao: SavingAccountDao = BankAppDatabase.getDatabase(application).savingAccountDao()
    private val currentAccountDao: CurrentAccountDao = BankAppDatabase.getDatabase(application).currentAccountDao()
    private val transactionDao: TransactionDao = BankAppDatabase.getDatabase(application).transactionDao()

    override fun getAccount(account_id: Long): Response<Account> {
        return try {
            val account = accountDao.getAccount(account_id)
            Response.Success(account)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    override fun updateAccount(account: Account): Response<Unit> {
        return try {
            Response.Success(accountDao.update(account))
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    override fun checkAccountExistence(account_id: Long): Boolean {
        return try {
            accountDao.accountExists(account_id)
        }catch (e: Exception){
            false
        }
    }


    override fun getTransactionHistory(account_id: Long): Response<List<Transaction>> {
        return try {
            val transaction = transactionDao.getTransactionsByAccount(account_id)
            Response.Success(transaction)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    override fun addTransaction(transaction: Transaction): Response<Unit> {
        return try {
            Response.Success(transactionDao.insertTransaction(transaction))
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    override fun getSavingAccount(account_id: Long): Response<Saving_Account> {
        return try {
            val savingAccount = savingAccountDao.getSavingAccount(account_id)
            Response.Success(savingAccount)
        }catch (e: Exception){
            Response.Error(e.message.toString())
        }
    }

    override fun getCurrentAccount(account_id: Long): Response<Current_Account> {
        return try {
            val currentAccount = currentAccountDao.getCurrentAccount(account_id)
            Response.Success(currentAccount)
        }catch (e: Exception){
            Response.Error(e.message.toString())
        }
    }

    override fun getUserAccounts(customerId: Int): Response<List<Account>?> {
        return try {
            val userAccounts = accountDao.getAllUserAccounts(customerId)
            Response.Success(userAccounts)
        }catch (e: Exception){
            Response.Error(e.message.toString())
        }
    }


}