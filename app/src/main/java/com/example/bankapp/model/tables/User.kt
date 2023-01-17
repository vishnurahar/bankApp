package com.example.bankapp.model.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val customer_id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "phone_number")
    val phone_number: Long,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "age")
    val age: Int
)

