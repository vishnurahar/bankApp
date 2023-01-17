package com.example.bankapp.storage.dao

import androidx.room.*
import com.example.bankapp.model.tables.Loan

@Dao
interface LoansDao {
    @Insert
    fun insert(loan: Loan)

    @Update
    fun update(loan: Loan)

    @Delete
    fun delete(loan: Loan)

    @Query("SELECT * FROM loans")
    fun getAll(): List<Loan>

    @Query("DELETE FROM loans")
    fun clearTable()
}
