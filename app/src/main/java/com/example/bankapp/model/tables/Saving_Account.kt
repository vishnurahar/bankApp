package com.example.bankapp.model.tables

import androidx.room.*

@Entity(tableName = "saving_accounts")
data class Saving_Account(
    @PrimaryKey
    @ColumnInfo(name = "account_id")
    val accountId: Long,

    @ColumnInfo(name = "atm_card_number")
    val atmCardNumber: Long,

    @ColumnInfo(name = "atm_card_expiry_date")
    val atmCardExpiryDate: String,

    @ColumnInfo(name = "atm_card_cvv")
    val atmCardCVV: Int,

    @ColumnInfo(name = "nrv")
    val NRV: Double = 0.0,

    @ColumnInfo(name = "interest_rate")
    val interestRate: Double = 6.0,

    @ColumnInfo(name = "nrv_charge")
    val NRVCharge: Double = 1000.0,

    @ColumnInfo(name = "minimum_deposit")
    val minimumDeposit: Double = 10000.0,

    @ColumnInfo(name = "minimum_age")
    val minimumAge: Int = 0,

    @ColumnInfo(name = "atm_transactions")
    val atmTransactions: Int = 5,

    @ColumnInfo(name = "atm_transaction_charge")
    val atmTransactionCharge: Double = 500.0,

    @ColumnInfo(name = "atm_transaction_limit")
    val atmTransactionLimit: Double = 20000.0,

    @ColumnInfo(name = "direct_transaction_limit")
    val directTransactionLimit: Double = Double.MAX_VALUE,

    @ColumnInfo(name = "daily_withdrawal_limit")
    val dailyWithdrawalLimit: Double = 50000.0
)



