package com.example.bankapp.model.tables

import androidx.room.*

@Entity(tableName = "loans")
data class Loan(
    @PrimaryKey val loan_id: Int,
    @ColumnInfo(name = "customer_id") val customer_id: Int,
    @ColumnInfo(name = "account_id") val account_id: Int,
    @ColumnInfo(name = "loan_type") val loan_type: String,
    @ColumnInfo(name = "loan_amount") val loan_amount: Double,
    @ColumnInfo(name = "interest_rate") val interest_rate: Double,
    @ColumnInfo(name = "loan_duration") val loan_duration: Int,
    @ColumnInfo(name = "remaining_loan_amount") val remaining_loan_amount: Double
)
