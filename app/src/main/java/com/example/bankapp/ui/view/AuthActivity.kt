package com.example.bankapp.ui.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.bankapp.R
import com.example.bankapp.databinding.ActivityAuthBinding
import com.example.bankapp.storage.Response
import com.example.bankapp.ui.viewmodels.AuthViewModel
import com.example.bankapp.util.PrefConstant

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)

        val viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        authViewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]

        val sharedPreferences = getSharedPreferences(PrefConstant.KEY_PREF, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        authViewModel.loginResult.observe(this) { result ->
            when (result) {
                is Response.Success<*> -> {
                    Toast.makeText(this, "Welcome ${result.data?.name}!", Toast.LENGTH_LONG).show()
                    editor.putBoolean(PrefConstant.KEY_IS_LOGGED_IN, true)
                    editor.putString(PrefConstant.KEY_EMAIL, result.data?.email)
                    editor.apply()
                    startActivity(Intent(this, MainActivity::class.java).putExtra(PrefConstant.KEY_EMAIL, binding.emailEditText.text.toString().trim()))
                    finish()
                }
                is Response.Error<*> -> {
                    Toast.makeText(this, result.errorMessage, Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        }

        authViewModel.signupResult.observe(this) { result ->
            when (result) {
                is Response.Success<*> -> {
                    Toast.makeText(this, "Signed Up successfully", Toast.LENGTH_SHORT).show()
//                    editor.putBoolean(PrefConstant.KEY_IS_LOGGED_IN, true)
//                    editor.apply()
//                    startActivity(Intent(this, MainActivity::class.java)
//                        .putExtra(PrefConstant.KEY_EMAIL, binding.emailEditText.text.toString().trim()))
//                    finish()
                    userLogin()
                }
                is Response.Error<*> -> {
                    Toast.makeText(this, result.errorMessage, Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }

        }

        binding.switchTextView.setOnClickListener {
            clearAllTextFields()
            with(binding) {
                if (switchTextView.text == resources.getString(R.string.sign_in)) {
                    headerTextView.text = resources.getString(R.string.sign_in)
                    bottomTextView.text = resources.getString(R.string.do_not_have_account)
                    switchTextView.text = resources.getString(R.string.sign_up)
                    ctaTextView.text = resources.getString(R.string.login)

                    nameEditText.visibility = View.INVISIBLE
                    phoneEditText.visibility = View.INVISIBLE
                    addressEditText.visibility = View.INVISIBLE
                    ageEditText.visibility = View.INVISIBLE
                } else {
                    headerTextView.text = resources.getString(R.string.sign_up)
                    bottomTextView.text = resources.getString(R.string.do_you_have_account)
                    switchTextView.text = resources.getString(R.string.sign_in)
                    ctaTextView.text = resources.getString(R.string.create_account)

                    nameEditText.visibility = View.VISIBLE
                    phoneEditText.visibility = View.VISIBLE
                    addressEditText.visibility = View.VISIBLE
                    ageEditText.visibility = View.VISIBLE
                }
            }
        }

        binding.ctaTextView.setOnClickListener {
            if (binding.ctaTextView.text == resources.getString(R.string.create_account)) {
                if (validateSignUpFields())
                    userSignUp()
            } else {
                if(validateLoginFields())
                    userLogin()
            }
        }
    }

    private fun clearAllTextFields() {
        with(binding){
            nameEditText.text = null
            emailEditText.text = null
            passwordEditText.text = null
            phoneEditText.text = null
            addressEditText.text = null
            ageEditText.text = null
        }
    }

    private fun userLogin() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        authViewModel.login(email, password)
    }

    private fun userSignUp() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val name = binding.nameEditText.text.toString().trim()
        val phone: Long = binding.phoneEditText.text.toString().trim().toLong()
        val address = binding.passwordEditText.text.toString().trim()
        val age = binding.ageEditText.text.toString().trim().toInt()
        authViewModel.signup(name, email, password, phone, address, age)
    }

    private fun validateLoginFields(): Boolean {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        if(email.length > 56) {
            Toast.makeText(this, "Max Length allowed is 56", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validateSignUpFields( ): Boolean {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val name = binding.nameEditText.text.toString().trim()
        val phone = binding.phoneEditText.text.toString().trim()
        val address = binding.addressEditText.text.toString().trim()
        val age = binding.ageEditText.text.toString().trim().toInt()
        if(email.length > 56) {
            Toast.makeText(this, "Max Length allowed is 56", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6 || password.length > 27){
            Toast.makeText(this, "Password Length, Min: 6 and Max: 27", Toast.LENGTH_SHORT).show()
            return false
        }
        if(name == ""){
            Toast.makeText(this, "Name Required", Toast.LENGTH_SHORT).show()
            return false
        }
        if(name.length>40){
            Toast.makeText(this, "Max Length allowed is 40 for Name", Toast.LENGTH_SHORT).show()
            return false
        }
        if(phone.length < 10){
            Toast.makeText(this, "Enter Valid Phone", Toast.LENGTH_SHORT).show()
            return false
        }
        if(address.length < 10){
            Toast.makeText(this, "Enter Valid Address", Toast.LENGTH_SHORT).show()
            return false
        }
        if(age < 1){
            Toast.makeText(this, "Invalid age", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}