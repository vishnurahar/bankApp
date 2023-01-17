package com.example.bankapp.storage.dao

import androidx.room.*
import com.example.bankapp.model.tables.Admin_Account

@Dao
interface AdminAccountDao {
    @Insert
    fun insert(admin_account: Admin_Account)

    @Update
    fun update(admin_account: Admin_Account)

    @Delete
    fun delete(admin_account: Admin_Account)

    @Query("SELECT * FROM admin_accounts")
    fun getAll(): List<Admin_Account>

    @Query("DELETE FROM admin_accounts")
    fun clearTable()
}