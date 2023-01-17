package com.example.bankapp.storage.dao

import androidx.room.*
import com.example.bankapp.model.tables.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    fun login(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email")
    fun getUserByEmail(email: String?): User?

    @Query("SELECT * FROM users WHERE customer_id = :customerId")
    fun getUserById(customerId: Int): User?

    @Query("SELECT * FROM users WHERE users.email LIKE :email")
    fun getAccount(email: String): User?

    @Query("DELETE FROM users")
    fun clearTable()
}