package com.example.bankapp.storage.dao

import androidx.room.*
import com.example.bankapp.model.tables.Saving_Account

@Dao
interface SavingAccountDao {
    @Insert
    fun insert(saving_account: Saving_Account)

    @Update
    fun update(saving_account: Saving_Account)

    @Delete
    fun delete(saving_account: Saving_Account)

    @Query("SELECT * FROM saving_accounts")
    fun getAll(): List<Saving_Account>

    @Query("SELECT * FROM saving_accounts WHERE account_id = :accountId")
    fun getSavingAccount(accountId: Long): Saving_Account

    @Query("DELETE FROM saving_accounts")
    fun clearTable()
}