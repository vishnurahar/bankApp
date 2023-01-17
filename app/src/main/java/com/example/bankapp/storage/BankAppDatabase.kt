package com.example.bankapp.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bankapp.model.tables.*
import com.example.bankapp.model.typeconverter.TransactionTypeConverter
import com.example.bankapp.storage.dao.*


@Database(
    entities = [
        User::class,
        Account::class,
        Saving_Account::class,
        Current_Account::class,
        Loan::class,
        Admin_Account::class,
        Referral_Code::class,
        Transaction::class,
    ],
    version = 1
)
@TypeConverters(TransactionTypeConverter::class)

abstract class BankAppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun accountsDao(): AccountDao
    abstract fun savingAccountDao(): SavingAccountDao
    abstract fun currentAccountDao(): CurrentAccountDao
    abstract fun loanDao(): LoansDao
    abstract fun adminAccountDao(): AdminAccountDao
    abstract fun referralCodeDao(): ReferralCodeDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: BankAppDatabase? = null

        fun getDatabase(context: Context): BankAppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BankAppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    suspend fun safeClearAllTables() {
        userDao().clearTable()
        accountsDao().clearTable()
        savingAccountDao().clearTable()
        currentAccountDao().clearTable()
        loanDao().clearTable()
        adminAccountDao().clearTable()
        referralCodeDao().clearTable()
        transactionDao().clearTable()
    }
}