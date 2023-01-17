package com.example.bankapp.model.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey
    @ColumnInfo(name = "account_id")
    val account_id: Long,

    @ColumnInfo(name = "customer_id")
    val customer_id: Int,

    @ColumnInfo(name = "account_type")
    val account_type: String,

    @ColumnInfo(name = "balance")
    var balance: Double,

    @ColumnInfo(name = "month_withdraw")
    var withdraw: Double = 0.0,

    @ColumnInfo(name = "transaction_history")
    val transaction_history: Transaction?
)