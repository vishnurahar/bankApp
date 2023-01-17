package com.example.bankapp.ui.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.bankapp.R
import com.example.bankapp.databinding.ActivityNewAccountOpenBinding
import com.example.bankapp.model.tables.*
import com.example.bankapp.storage.Response
import com.example.bankapp.ui.viewmodels.NewAccountViewModel
import com.example.bankapp.util.PrefConstant
import com.google.gson.Gson
import java.util.*

class NewAccountOpenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewAccountOpenBinding
    private var customerId: Int? = null
    private lateinit var newAccountViewModel: NewAccountViewModel
    private var newAccountNumber: Long? = null
    private var currentUser : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_account_open)

        val viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        newAccountViewModel =
            ViewModelProvider(this, viewModelFactory)[NewAccountViewModel::class.java]

        val accountType: String? = intent.getStringExtra(PrefConstant.KEY_ACCOUNT_TYPE)
        customerId = intent.getIntExtra("customerId", -1)

        getUser()
        setConditions(accountType)

        binding.openAccTextView.setOnClickListener {
            openAccount(customerId!!, accountType)
        }
        newAccountViewModel.getUserResult.observe(this){ result ->
            when(result){
                is Response.Success -> {
                    currentUser = result.data
                }else -> {
                    Toast.makeText(this, "Unable to find current user try again", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }

        newAccountViewModel.newAccountResult.observe(this) { result ->
            when (result) {
                is Response.Success -> {
                    Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                    getAccount(newAccountNumber)
                }
                is Response.Error -> {
                    Toast.makeText(
                        this,
                        "Error Creating Account: ${result.errorMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(
                        this,
                        "Error Creating Account: ${result.errorMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        newAccountViewModel.getAccountResult.observe(this){ account ->
            when(account){
                is Response.Success ->{
                    if (account.data?.account_type == PrefConstant.KEY_SAVINGS_ACCOUNT){
                        createSavingsAccount(account.data.account_id)
                    }
                    else if (account.data?.account_type == PrefConstant.KEY_CURRENT_ACCOUNT){
                        createCurrentAccount(account.data.account_id)
                    }
                }
                else -> {
                    Toast.makeText(this, "Error in fetching Account: ${account.errorMessage}", Toast.LENGTH_SHORT).show()
                }
            }

        }

        newAccountViewModel.newSavingsAccountResult.observe(this){ savingAccount ->
            when(savingAccount) {
                is Response.Success -> {
                    val intent = Intent(this, AccountDetailActivity::class.java)
                    intent.putExtra(PrefConstant.KEY_ACCOUNT_TYPE, PrefConstant.KEY_SAVINGS_ACCOUNT)
                    intent.putExtra(PrefConstant.KEY_ACCOUNT_ID, newAccountNumber)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    // Unable to open savings account
                }
            }
        }

        newAccountViewModel.newCurrentAccountResult.observe(this){ currentAccount ->
            when(currentAccount) {
                is Response.Success -> {
                    val intent = Intent(this, AccountDetailActivity::class.java)
                    intent.putExtra(PrefConstant.KEY_ACCOUNT_TYPE, PrefConstant.KEY_CURRENT_ACCOUNT)
                    intent.putExtra(PrefConstant.KEY_ACCOUNT_ID, newAccountNumber)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    // Unable to open current account
                }
            }
        }
    }

    private fun createCurrentAccount(accountId: Long) {
        newAccountViewModel.newCurrentAccount(Current_Account(
            accountId,
            0.0  // initial NRV will be zero
        ))

    }

    private fun getUser() {
        customerId?.let { newAccountViewModel.getUser(it) }
    }

    private fun createSavingsAccount(accountId: Long) {
        val atmCardObj = ATM_Card()
        atmCardObj.generateCard()
        val atmCardNumber = atmCardObj.cardNumber
        val atmCardCVV = atmCardObj.cvv
        val atmCardExpiry = atmCardObj.expiryDate

        newAccountViewModel.newSavingsAccount(Saving_Account(
            accountId,
            atmCardNumber,
            atmCardExpiry,
            atmCardCVV,
            0.0  // initial NRV
        ))
    }

    private fun getAccount(newAccountNumber: Long?) {
        if (newAccountNumber != null) {
            newAccountViewModel.getAccount(newAccountNumber)
        }
    }


    private fun openAccount(customerId: Int, accountType: String?) {
        val initialBalance = binding.initialBalanceEditText.text.toString().trim().toDouble()
        val accountNumber: Long = generateAccountNumber()
        newAccountNumber = accountNumber
        val initialTransaction = Transaction(
            0, accountNumber, currentDate(), PrefConstant.KEY_TRANSACTION_CREDIT, initialBalance, initialBalance
        )
        if (accountType== PrefConstant.KEY_SAVINGS_ACCOUNT){
            if (accountType.isNotEmpty() && checkSavingRequirement(initialBalance)) {
                newAccountViewModel.newAccount(
                    Account(
                        accountNumber,
                        customerId,
                        accountType,
                        initialBalance,
                        0.0,
                        initialTransaction
                    )
                )
                newAccountViewModel.addTransaction(initialTransaction)
            }
        }else if (accountType == PrefConstant.KEY_CURRENT_ACCOUNT){
            if (accountType.isNotEmpty() && checkCurrentRequirement(initialBalance)) {
                newAccountViewModel.newAccount(
                    Account(
                        accountNumber,
                        customerId,
                        accountType,
                        initialBalance,
                        0.0,
                        initialTransaction
                    )
                )
                newAccountViewModel.addTransaction(initialTransaction)
            }
        }
    }

    private fun checkSavingRequirement(initialBalance: Double): Boolean {
        if (initialBalance < 10000) {
            Toast.makeText(this, "Minimum Initial Deposit is: 10000", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkCurrentRequirement(initialBalance: Double): Boolean {
        if (initialBalance < 100000) {
            Toast.makeText(this, "Minimum Initial Deposit is: 100000", Toast.LENGTH_SHORT).show()
            return false
        }
        if (currentUser?.age!! < 18){
            Toast.makeText(this, "Minimum age is: 18 to open Current Account", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun setConditions(accountType: String?) {
        binding.selectAccTypeTextView.text = resources.getString(R.string.account_type, accountType)

        if (accountType == PrefConstant.KEY_SAVINGS_ACCOUNT) {
            binding.interestValueTextView.text = "6%"
            binding.nrvValueTextView.text = "100000"
        } else if (accountType == PrefConstant.KEY_CURRENT_ACCOUNT) {
            binding.interestValueTextView.text = "0%"
            binding.nrvValueTextView.text = "500000"
        }
    }

    private fun generateAccountNumber(): Long {
        val random = Random()
        val accNumber = StringBuilder()
        for (i in 0 until 10) {
            accNumber.append(random.nextInt(10))
        }
        return accNumber.toString().toLong()
    }

    fun currentDate(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        return "$day/$month/$year"
    }

}

class ATM_Card {
    var cardNumber: Long = 0
    var cvv: Int = 0
    var expiryDate: String = ""

    fun generateCard() {
        cardNumber = generateCardNumber()
        cvv = generateCVV()
        expiryDate = generateExpiryDate()
    }

    private fun generateCardNumber(): Long {
        val random = Random()
        val cardNumber = StringBuilder()
        for (i in 0 until 16) {
            cardNumber.append(random.nextInt(10))
        }
        return cardNumber.toString().toLong()
    }

    private fun generateCVV(): Int {
        val random = Random()
        val cvv = StringBuilder()
        for (i in 0 until 3) {
            cvv.append(random.nextInt(10))
        }
        return cvv.toString().toInt()
    }

    private fun generateExpiryDate(): String {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val expiryYear = currentYear + 5
        val expiryMonth = currentMonth + 1
        return "$expiryMonth/$expiryYear"
    }
}