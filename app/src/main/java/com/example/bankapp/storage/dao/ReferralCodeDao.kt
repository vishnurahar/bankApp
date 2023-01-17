package com.example.bankapp.storage.dao

import androidx.room.*
import com.example.bankapp.model.tables.Referral_Code

@Dao
interface ReferralCodeDao {
    @Insert
    fun insert(referral_code: Referral_Code)

    @Update
    fun update(referral_code: Referral_Code)

    @Delete
    fun delete(referral_code: Referral_Code)

    @Query("SELECT * FROM referral_codes")
    fun getAll(): List<Referral_Code>

    @Query("DELETE FROM referral_codes")
    fun clearTable()
}
