package com.example.bankapp.model.tables

import androidx.room.*

@Entity(tableName = "admin_accounts")
data class Admin_Account(
    @PrimaryKey val admin_id: Int,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "password") val password: String
)

