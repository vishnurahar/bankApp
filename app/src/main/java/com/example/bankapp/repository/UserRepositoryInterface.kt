package com.example.bankapp.repository

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.bankapp.model.tables.Account
import com.example.bankapp.model.tables.User
import com.example.bankapp.storage.Response

interface UserRepositoryInterface {
    fun getUserAccount(customerId: Int): Response<List<Account>?>

    fun getCurrentUser(email: String?) : Response<User?>

    fun getCurrentUserById(customerId: Int) : Response<User?>
}