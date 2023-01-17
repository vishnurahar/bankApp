package com.example.bankapp.storage.dao

import androidx.room.*

@Dao
interface TransactionDao {
    @Insert
    fun insertTransaction(transaction: com.example.bankapp.model.tables.Transaction)

    @Update
    fun updateTransaction(transaction: com.example.bankapp.model.tables.Transaction)

    @Delete
    fun deleteTransaction(transaction: com.example.bankapp.model.tables.Transaction)

    @Query("SELECT * FROM transaction_table")
    fun getAllTransactions(): List<com.example.bankapp.model.tables.Transaction>

    @Query("SELECT * FROM transaction_table WHERE transactionId = :id")
    fun getTransaction(id: Int): com.example.bankapp.model.tables.Transaction

    @Query("SELECT * FROM transaction_table WHERE account_id = :accountId")
    fun getTransactionsByAccount(accountId: Long): List<com.example.bankapp.model.tables.Transaction>

    @Query("DELETE FROM referral_codes")
    fun clearTable()
}
