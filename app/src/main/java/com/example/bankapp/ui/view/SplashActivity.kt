package com.example.bankapp.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.bankapp.R
import com.example.bankapp.util.PrefConstant

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    var SPLASH_TIME_OUT = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val sharedPreferences = getSharedPreferences(PrefConstant.KEY_PREF, Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean(PrefConstant.KEY_IS_LOGGED_IN, false)
            && sharedPreferences.getString(PrefConstant.KEY_EMAIL, "")!!.isNotBlank() ) {
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, SPLASH_TIME_OUT.toLong())
        } else {
            Handler().postDelayed({
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }, SPLASH_TIME_OUT.toLong())
        }

    }
}