package com.example.bankapp

import android.app.Application
import android.content.Context

class BankApplication : Application() {

    companion object {
        lateinit var mContext: Context

        fun getContext(): Context {
            return mContext
        }
    }

}