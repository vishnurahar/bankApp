package com.example.bankapp.model.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Int,

    @ColumnInfo(name = "account_id")
    val accountId: Long,

    @ColumnInfo(name = "transaction_time")
    val transactionTime: String,

    @ColumnInfo(name = "transaction_type")
    val transactionType: String,

    @ColumnInfo(name = "transaction_amount")
    val transactionAmount: Double,

    @ColumnInfo(name = "net_balance")
    val netBalance: Double
)

