package com.example.bankapp.repository

import android.app.Application
import com.example.bankapp.model.tables.User
import com.example.bankapp.storage.BankAppDatabase
import com.example.bankapp.storage.Response
import com.example.bankapp.storage.dao.UserDao

class AuthRepository(
    application: Application
) : AuthRepositoryInterface {

    private val userDao: UserDao = BankAppDatabase.getDatabase(application).userDao()

    override fun login(email: String, password: String): Response<User> {

        return try {
            val user = userDao.login(email, password)
            if (user != null) {
                Response.Success(user)
            } else {
                Response.Error("Invalid email or password")
            }
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    override fun signUp(name: String, email: String, password: String, phone: Long, address: String, age: Int): Response<Unit> {
        return try {
            userDao.insert(User(0, name, email, password, phone, address, age))
            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    fun isValidAccount(email: String, password: String): Boolean {

        val user = userDao.getAccount(email)
        if (user != null) return user.password == password

        return false
    }

    override fun getLoggedInUser(email:String): Response<User>{
        return try {
            val user = userDao.getAccount(email)
            if (user != null) {
                Response.Success(user)
            } else {
                Response.Error("Invalid user email")
            }
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

}