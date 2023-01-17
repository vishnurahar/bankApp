package com.example.bankapp.storage.dao

import androidx.room.*
import com.example.bankapp.model.tables.Current_Account
import com.example.bankapp.model.tables.Saving_Account

@Dao
interface CurrentAccountDao {
    @Insert
    fun insert(current_account: Current_Account)

    @Update
    fun update(current_account: Current_Account)

    @Delete
    fun delete(current_account: Current_Account)

    @Query("SELECT * FROM current_accounts")
    fun getAll(): List<Current_Account>

    @Query("SELECT * FROM current_accounts WHERE account_id = :accountId")
    fun getCurrentAccount(accountId: Long): Current_Account

    @Query("DELETE FROM current_accounts")
    fun clearTable()
}
