package com.example.bankapp.storage.dao

import androidx.room.*
import com.example.bankapp.model.tables.Account

@Dao
interface AccountDao {
    @Insert
    fun insert(account: Account)

    @Update
    fun update(account: Account)

    @Delete
    fun delete(account: Account)

    @Query("SELECT * FROM accounts")
    fun getAll(): List<Account>

    @Query("SELECT * FROM accounts WHERE customer_id = :customer_id")
    fun getAllUserAccounts(customer_id: Int): List<Account>?

    @Query("SELECT * FROM accounts WHERE account_id = :account_id")
    fun getAccount(account_id: Long): Account

    @Query("SELECT EXISTS(SELECT 1 FROM accounts WHERE account_id = :accountId)")
    fun accountExists(accountId: Long): Boolean

    @Query("DELETE FROM accounts")
    fun clearTable()
}