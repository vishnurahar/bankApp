package com.example.bankapp.model.typeconverter

import androidx.room.TypeConverter
import com.example.bankapp.model.tables.Transaction
import com.google.gson.Gson

class TransactionTypeConverter {

    @TypeConverter
    fun fromTransaction(transaction: Transaction): String {
        return Gson().toJson(transaction)
    }

    @TypeConverter
    fun toTransaction(data: String): Transaction {
        return Gson().fromJson(data, Transaction::class.java)
    }
}