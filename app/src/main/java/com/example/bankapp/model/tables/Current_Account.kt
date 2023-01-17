package com.example.bankapp.model.tables

import androidx.room.*


@Entity(tableName = "current_accounts")
data class Current_Account(
    @PrimaryKey
    @ColumnInfo(name = "account_id")
    val accountId: Long,

    @ColumnInfo(name = "nrv")
    val NRV: Double = 500000.0,

    @ColumnInfo(name = "nrv_charge")
    val NRVCharge: Double = 5000.0,

    @ColumnInfo(name = "minimum_deposit")
    val minimumDeposit: Double = 100000.0,

    @ColumnInfo(name = "minimum_age")
    val minimumAge: Int = 18,

    @ColumnInfo(name = "transaction_charge")
    val transactionCharge: Double = 0.5,

    @ColumnInfo(name = "maximum_charge_per_transaction")
    val maxChargePerTransaction: Double = 500.0,

    @ColumnInfo(name = "transaction_penalty")
    val transactionPenalty: Double = 500.0
)


