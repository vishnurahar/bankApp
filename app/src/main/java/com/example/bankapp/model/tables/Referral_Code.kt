package com.example.bankapp.model.tables

import androidx.room.*

@Entity(tableName = "referral_codes")
data class Referral_Code(
    @PrimaryKey val referral_id: Int,
    @ColumnInfo(name = "customer_id") val customer_id: Int,
    @ColumnInfo(name = "referral_code") val referral_code: String
)
