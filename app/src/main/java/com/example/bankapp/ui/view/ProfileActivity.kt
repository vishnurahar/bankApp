package com.example.bankapp.ui.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.bankapp.R
import com.example.bankapp.databinding.ActivityProfileBinding
import com.example.bankapp.storage.Response
import com.example.bankapp.ui.viewmodels.MainViewModel
import com.example.bankapp.ui.viewmodels.ProfileViewModel
import com.example.bankapp.util.PrefConstant

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private var customerId : Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        val sPref = getSharedPreferences(PrefConstant.KEY_PREF, Context.MODE_PRIVATE)
        val editor = sPref.edit()
        customerId = intent.getIntExtra("customerId", 0)

        val viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        profileViewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        getUserInfo(customerId!!)

        profileViewModel.currentUser.observe(this){ result ->
            when(result){
                is Response.Success ->{
                    binding.textViewNameValue.text = result.data?.name
                    binding.textViewEmailValue.text = result.data?.email
                    binding.textViewPhoneValue.text = result.data?.phone_number.toString()
                    binding.textViewAgeValue.text = result.data?.age.toString()
                }
                else ->{

                }
            }

        }


        binding.logoutTextView.setOnClickListener {
            editor.clear().apply()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }

    private fun getUserInfo(customerId: Int) {
        profileViewModel.getLoggedInUser(customerId)
    }
}