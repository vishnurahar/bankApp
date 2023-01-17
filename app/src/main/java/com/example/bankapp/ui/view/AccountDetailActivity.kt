package com.example.bankapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bankapp.R
import com.example.bankapp.databinding.ActivityAccountDetailBinding
import com.example.bankapp.databinding.AddMoneyBottomSheetBinding
import com.example.bankapp.databinding.AtmBottomSheetBinding
import com.example.bankapp.databinding.TransferBottomSheetBinding
import com.example.bankapp.model.tables.Account
import com.example.bankapp.model.tables.Current_Account
import com.example.bankapp.model.tables.Saving_Account
import com.example.bankapp.model.tables.Transaction
import com.example.bankapp.storage.Response
import com.example.bankapp.ui.adapter.TransactionListAdapter
import com.example.bankapp.ui.viewmodels.AccountDetailViewModel
import com.example.bankapp.util.PrefConstant
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

class AccountDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountDetailBinding
    private lateinit var accountDetailViewModel: AccountDetailViewModel
    private val transactionListAdapter by lazy { TransactionListAdapter() }
    private var savingsAccount: Saving_Account? = null
    private var currentAccount: Current_Account? = null
    private var sourceAccount: Account? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account_detail)

        val viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        accountDetailViewModel =
            ViewModelProvider(this, viewModelFactory)[AccountDetailViewModel::class.java]

        val accountType = intent.getStringExtra(PrefConstant.KEY_ACCOUNT_TYPE)
        val accountNumber = intent.getLongExtra(PrefConstant.KEY_ACCOUNT_ID, 0)

        if (accountType.equals(PrefConstant.KEY_CURRENT_ACCOUNT)) {
            updateCurrentAccount(accountNumber)
        } else if (accountType.equals(PrefConstant.KEY_SAVINGS_ACCOUNT)) {
            updateSavingAccount(accountNumber)
        }

        binding.accTypeTextView.text = resources.getString(R.string.account_type, accountType)

        val recyclerView = binding.transactionRv
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = transactionListAdapter

        fetchTransactionHistory(accountNumber)

        accountDetailViewModel.savingAccountResult.observe(this) { result ->
            when (result) {
                is Response.Success -> {
                    savingsAccount = result.data
                    binding.atmCardNumber.text =
                        result.data?.atmCardNumber.toString().chunked(4).joinToString(" ")
                    binding.atmExpiryDate.text = result.data?.atmCardExpiryDate
                    binding.atmCvvTextView.text = result.data?.atmCardCVV.toString()
                    binding.nrvValue.text = result.data?.NRV.toString()
                }
                else -> {

                }
            }
        }

        accountDetailViewModel.currentAccountResult.observe(this) { result ->
            when (result) {
                is Response.Success -> {
                    currentAccount = result.data
                    if (result.data?.NRV != null)
                        binding.nrvValue.text = result.data.NRV.toString()
                    else
                        binding.nrvValue.text = "0"
                }
                else -> {

                }
            }
        }
        accountDetailViewModel.transactionResult.observe(this) {
            when (it) {
                is Response.Success -> {
                    transactionListAdapter.submitList(it.data?.toMutableList()?.reversed())
                }
                else -> {

                }
            }
        }

        accountDetailViewModel.accountResult.observe(this) { result ->
            when (result) {
                is Response.Success -> {
                    sourceAccount = result.data
                    binding.accountBalance.text = resources.getString(
                        R.string.acc_balance,
                        result.data?.balance.toString()
                    )

                    if (result.data?.account_type.equals(PrefConstant.KEY_SAVINGS_ACCOUNT)) {
                        binding.atmLogoImageView.visibility = View.VISIBLE
                        binding.atmExpiryDate.visibility = View.VISIBLE
                        binding.atmCardNumber.visibility = View.VISIBLE
                        binding.atmCvvTextView.visibility = View.VISIBLE
                        binding.atmTransfer.visibility = View.VISIBLE
                    } else if (result.data?.account_type.equals(PrefConstant.KEY_CURRENT_ACCOUNT)) {
                        binding.atmLogoImageView.visibility = View.GONE
                        binding.atmExpiryDate.visibility = View.GONE
                        binding.atmCardNumber.visibility = View.GONE
                        binding.atmCvvTextView.visibility = View.GONE
                        binding.atmTransfer.visibility = View.GONE
                    }
                }
                else -> {

                }
            }
        }

        accountDetailViewModel.updateAccountResult.observe(this) {
            when (it) {
                is Response.Success -> {
                    if (accountType.equals(PrefConstant.KEY_CURRENT_ACCOUNT)) {
                        updateCurrentAccount(accountNumber)
                    } else if (accountType.equals(PrefConstant.KEY_SAVINGS_ACCOUNT)) {
                        updateSavingAccount(accountNumber)
                    }
                    fetchTransactionHistory(accountNumber)
                }
                is Response.Error -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Account Info Not Updated", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


        binding.transferCard.setOnClickListener {
            val transferBottomSheet =
                BottomSheetDialog(this@AccountDetailActivity, R.style.BottomSheetDialogTheme)
            val bindingTransferSheet = DataBindingUtil.inflate<TransferBottomSheetBinding>(
                layoutInflater,
                R.layout.transfer_bottom_sheet,
                null,
                false
            )

            bindingTransferSheet.transferBtn.setOnClickListener {
                val toAccountNumber =
                    bindingTransferSheet.accountNumberEditView.text.toString().trim().toLong()
                val transferAmount = bindingTransferSheet.amountEditView.text.toString().toDouble()
                getDestinationAccountInfo(toAccountNumber)

                accountDetailViewModel.destinationAccount.observe(this) { destinationAcc ->
                    when (destinationAcc) {
                        is Response.Success -> {
                            if (sourceAccount?.balance!! < transferAmount) {
                                Toast.makeText(this, "Insufficient Funds", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                if (destinationAcc.data != null) {
                                    addTransaction(
                                        sourceAccount!!,
                                        destinationAcc.data,
                                        transferAmount
                                    )
                                    transferBottomSheet.dismiss()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Transaction Failed, Try Again!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        is Response.Error -> {
                            Toast.makeText(
                                this,
                                "Destination Account Not Found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            Toast.makeText(
                                this,
                                "Destination Account Not Found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            transferBottomSheet.setContentView(bindingTransferSheet.root)
            transferBottomSheet.show()

        }

        binding.atmTransfer.setOnClickListener {
            val atmBottomSheet =
                BottomSheetDialog(this@AccountDetailActivity, R.style.BottomSheetDialogTheme)
            val bindingAtmSheet = DataBindingUtil.inflate<AtmBottomSheetBinding>(
                layoutInflater, R.layout.atm_bottom_sheet, null, false
            )

            bindingAtmSheet.withdrawBtn.setOnClickListener {
                val atmCardNumber =
                    bindingAtmSheet.atmNumberEditView.text.toString().trim().toLong()
                val withdrawAmount = bindingAtmSheet.amountEditView.text.toString().toDouble()
                val atmCvv = bindingAtmSheet.cvvEditView.text.toString().toInt()

                if (withdrawMoney(atmCardNumber, withdrawAmount, atmCvv)){
                    updateAtmWithdraw(sourceAccount!!, withdrawAmount)
                    atmBottomSheet.dismiss()
                }

            }

            atmBottomSheet.setContentView(bindingAtmSheet.root)
            atmBottomSheet.show()
        }

        binding.addMoneyCard.setOnClickListener {
            val addMoneyBottomSheet =
                BottomSheetDialog(this@AccountDetailActivity, R.style.BottomSheetDialogTheme)
            val bindingAddMoneySheet = DataBindingUtil.inflate<AddMoneyBottomSheetBinding>(
                layoutInflater, R.layout.add_money_bottom_sheet, null, false
            )
            bindingAddMoneySheet.depositBtn.setOnClickListener {
                val depositAmount = bindingAddMoneySheet.amountEditView.text.toString().toDouble()
                if (depositAmount > 0 && sourceAccount != null){
                    addMoneyToAccount(sourceAccount!!, depositAmount)
                    addMoneyBottomSheet.dismiss()
                }else{
                    Toast.makeText(this, "Invalid Deposit Amount ", Toast.LENGTH_SHORT).show()
                }
            }

            addMoneyBottomSheet.setContentView(bindingAddMoneySheet.root)
            addMoneyBottomSheet.show()
        }

    }

    private fun addMoneyToAccount(sourceAccount: Account, depositAmount: Double) {
        sourceAccount.balance  += depositAmount

        val depositTransaction = Transaction(
            0,
            sourceAccount.account_id,
            currentDate(),
            PrefConstant.KEY_TRANSACTION_CREDIT,
            depositAmount,
            sourceAccount.balance,
        )

        accountDetailViewModel.updateAccount(sourceAccount)
        accountDetailViewModel.addTransaction(depositTransaction)

    }

    private fun withdrawMoney(atmCardNumber: Long, withdrawAmount: Double, atmCvv: Int): Boolean {
        if (atmCardNumber != savingsAccount?.atmCardNumber) {
            Toast.makeText(this, "Invalid Card Number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (withdrawAmount > 20000) {
            Toast.makeText(this, "Max Limit Per Transaction is 20000", Toast.LENGTH_SHORT).show()
            return false
        }
        if (withdrawAmount > sourceAccount?.balance!!) {
            Toast.makeText(this, "Insufficient Funds", Toast.LENGTH_SHORT).show()
            return false
        }
        if (atmCvv != savingsAccount?.atmCardCVV){
            Toast.makeText(this, "Invalid CVV", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun addTransaction(
        sourceAccount: Account,
        destinationAccount: Account,
        transferAmount: Double
    ) {
        sourceAccount.balance -= transferAmount
        destinationAccount.balance += transferAmount

        val sourceTransaction = Transaction(
            0,
            sourceAccount.account_id,
            currentDate(),
            PrefConstant.KEY_TRANSACTION_DEBIT,
            transferAmount,
            sourceAccount.balance,
        )

        val destinationTransaction = Transaction(
            0,
            destinationAccount.account_id,
            currentDate(),
            PrefConstant.KEY_TRANSACTION_CREDIT,
            transferAmount,
            destinationAccount.balance,
        )

        accountDetailViewModel.updateAccount(sourceAccount)
        accountDetailViewModel.updateAccount(destinationAccount)
        accountDetailViewModel.addTransaction(sourceTransaction)
        accountDetailViewModel.addTransaction(destinationTransaction)
    }

    private fun updateAtmWithdraw(userAccount: Account, withdrawAmount: Double){

        userAccount.balance -= withdrawAmount
        val atmWithdrawTransaction = Transaction(
            0,
            userAccount.account_id,
            currentDate(),
            PrefConstant.KEY_TRANSACTION_DEBIT,
            withdrawAmount,
            userAccount.balance,
        )

        accountDetailViewModel.updateAccount(userAccount)
        accountDetailViewModel.addTransaction(atmWithdrawTransaction)
    }

    private fun currentDate(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        return "$day/$month/$year"
    }

    private fun getDestinationAccountInfo(toAccountNumber: Long) {
        accountDetailViewModel.getDestinationAccount(toAccountNumber)
    }

    private fun fetchTransactionHistory(accountNumber: Long) {
        accountDetailViewModel.getTransactionHistory(accountNumber)
    }

    private fun updateSavingAccount(accountNumber: Long) {
        accountDetailViewModel.getSavingAccount(accountNumber)
    }

    private fun updateCurrentAccount(accountNumber: Long) {
        accountDetailViewModel.getCurrentAccount(accountNumber)
    }
}