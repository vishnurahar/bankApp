package com.example.bankapp.ui.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bankapp.R
import com.example.bankapp.databinding.AccountOptionBottomSheetBinding
import com.example.bankapp.databinding.ActivityMainBinding
import com.example.bankapp.model.tables.Account
import com.example.bankapp.model.tables.User
import com.example.bankapp.storage.Response
import com.example.bankapp.ui.adapter.AccountListAdapter
import com.example.bankapp.ui.viewmodels.AuthViewModel
import com.example.bankapp.ui.viewmodels.MainViewModel
import com.example.bankapp.util.PrefConstant
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val accountListAdapter by lazy { AccountListAdapter() }
    private lateinit var mainViewModel: MainViewModel
    var customer_id: Int? = null
    private var showLoanButton: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val sharedPreferences = getSharedPreferences(PrefConstant.KEY_PREF, Context.MODE_PRIVATE)
        val email = sharedPreferences.getString(PrefConstant.KEY_EMAIL, "")
        val editor = sharedPreferences.edit()
        editor.putString(PrefConstant.KEY_EMAIL, email)
        editor.apply()

        val viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        getCurrentUser(email)

        val recyclerView = binding.accountListRecyclerView
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = accountListAdapter

        binding.profileCardView.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java).putExtra("customerId", customer_id))
        }

        binding.createNewAccTextView.setOnClickListener {

            val bottomSheet = BottomSheetDialog(this@MainActivity, R.style.BottomSheetDialogTheme)
            val bindingSheet = DataBindingUtil.inflate<AccountOptionBottomSheetBinding>(
                layoutInflater,
                R.layout.account_option_bottom_sheet,
                null,
                false
            )
            if (!showLoanButton) {
                bindingSheet.loanAccTextView.visibility = View.GONE
            } else {
                bindingSheet.loanAccTextView.setOnClickListener {
                    val intent = Intent(this, NewLoanAccountActivity::class.java)
                    intent.putExtra(PrefConstant.KEY_ACCOUNT_TYPE, PrefConstant.KEY_LOAN_ACCOUNT)
                    intent.putExtra("customerId", customer_id)
                    startActivity(intent)
                    bottomSheet.dismiss()
                }
            }
            bindingSheet.savingsAccTextView.setOnClickListener {
                val intent = Intent(this, NewAccountOpenActivity::class.java)
                intent.putExtra(PrefConstant.KEY_ACCOUNT_TYPE, PrefConstant.KEY_SAVINGS_ACCOUNT)
                intent.putExtra("customerId", customer_id)
                startActivity(intent)
                bottomSheet.dismiss()
            }
            bindingSheet.currentAccTextView.setOnClickListener {
                val intent = Intent(this, NewAccountOpenActivity::class.java)
                intent.putExtra(PrefConstant.KEY_ACCOUNT_TYPE, PrefConstant.KEY_CURRENT_ACCOUNT)
                intent.putExtra("customerId", customer_id)
                startActivity(intent)
                bottomSheet.dismiss()
            }
            bottomSheet.setContentView(bindingSheet.root)
            bottomSheet.show()

        }
    }

    override fun onResume() {
        mainViewModel.currentUser.observe(this) { result ->
            when (result) {
                is Response.Success -> {
                    Toast.makeText(this, "Welcome ${result.data?.name}!", Toast.LENGTH_LONG).show()
                    customer_id = result.data?.customer_id
                    customer_id?.let { getUserAccounts(it) }
                }
                is Response.Error -> {
                    Toast.makeText(this, result.errorMessage, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        mainViewModel.userAccounts.observe(this) { accounts ->
            when (accounts) {
                is Response.Success -> {
                    if (accounts.data?.isNotEmpty() == true) {
                        val list = accounts.data.toMutableList()
                        accountListAdapter.submitList(list)
                        showLoanButton = true
                        binding.accountListRecyclerView.visibility = View.VISIBLE
                        binding.noAccountTextView.visibility = View.INVISIBLE
                    } else {
                        binding.noAccountTextView.visibility = View.VISIBLE
                        binding.accountListRecyclerView.visibility = View.INVISIBLE
                    }
                }
                is Response.Error -> {
                    binding.noAccountTextView.visibility = View.VISIBLE
                    showLoanButton = false
                }
                else -> {
                    binding.noAccountTextView.visibility = View.VISIBLE
                    showLoanButton = false
                }
            }
        }
        super.onResume()
    }

    private fun getUserAccounts(customerId: Int) {
        mainViewModel.getUserAccounts(customerId)
    }

    private fun getCurrentUser(email: String?) {
        mainViewModel.getLoggedInUser(email)
    }
}