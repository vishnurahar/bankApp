package com.example.bankapp.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.bankapp.R
import com.example.bankapp.databinding.ActivityNewLoanAccountBinding
import com.example.bankapp.model.tables.User
import com.example.bankapp.ui.viewmodels.NewAccountViewModel
import com.example.bankapp.ui.viewmodels.NewLoanAccountViewModel
import com.example.bankapp.util.PrefConstant

class NewLoanAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewLoanAccountBinding
    private lateinit var newLoanAccountViewModel: NewLoanAccountViewModel
    private var customerId: Int? = null
    private var newAccountNumber: Long? = null
    private var currentUser : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_loan_account)

        val viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        newLoanAccountViewModel =
            ViewModelProvider(this, viewModelFactory)[NewLoanAccountViewModel::class.java]

        val accountType: String? = intent.getStringExtra(PrefConstant.KEY_ACCOUNT_TYPE)
        customerId = intent.getIntExtra("customerId", -1)
    }
}