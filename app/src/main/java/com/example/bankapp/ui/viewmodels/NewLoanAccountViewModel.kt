package com.example.bankapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.bankapp.repository.NewAccountRepository
import com.example.bankapp.repository.UserRepository

class NewLoanAccountViewModel(application: Application) : AndroidViewModel(application) {

    private val newAccountRepository: NewAccountRepository = NewAccountRepository(application)
    private val userRepository: UserRepository = UserRepository(application)
}