package com.example.bankapp.repository

import android.app.Application
import com.example.bankapp.model.tables.Account
import com.example.bankapp.model.tables.User
import com.example.bankapp.storage.BankAppDatabase
import com.example.bankapp.storage.Response
import com.example.bankapp.storage.dao.AccountDao
import com.example.bankapp.storage.dao.UserDao

class UserRepository(
    application: Application
) : UserRepositoryInterface {

    private val accountDao: AccountDao = BankAppDatabase.getDatabase(application).accountsDao()
    private val userDao: UserDao = BankAppDatabase.getDatabase(application).userDao()

    override fun getUserAccount(customerId: Int): Response<List<Account>?> {
        return try {
            val accounts = accountDao.getAllUserAccounts(customerId)
            if (accounts != null) {
                Response.Success(accounts)
            } else {
                Response.Error("Invalid Customer ID")
            }
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    override fun getCurrentUser(email: String?): Response<User?> {
        return try {
            val user = userDao.getUserByEmail(email)
            if (user != null){
                Response.Success(user)
            } else {
                Response.Error("user not found")
            }
        } catch (e: Exception){
            Response.Error(e.message.toString())
        }
    }

    override fun getCurrentUserById(customerId: Int): Response<User?> {
        return try {
            val user = userDao.getUserById(customerId)
            if (user != null){
                Response.Success(user)
            } else {
                Response.Error("user not found")
            }
        } catch (e: Exception){
            Response.Error(e.message.toString())
        }
    }


}