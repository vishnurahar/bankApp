package com.example.bankapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bankapp.R
import com.example.bankapp.databinding.AccountListLayoutBinding
import com.example.bankapp.model.tables.Account
import com.example.bankapp.ui.view.AccountDetailActivity


class AccountListAdapter : ListAdapter<Account, AccountListAdapter.AccountListViewHolder>(ACCOUNT_LIST_COMPARATOR) {

    private var context: Context? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountListAdapter.AccountListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AccountListLayoutBinding.inflate(inflater, parent, false)
        context = parent.context
        return AccountListViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: AccountListAdapter.AccountListViewHolder, position: Int) {
        val account = getItem(position)
        holder.bind(account)
    }


    inner class AccountListViewHolder(
        private val binding: AccountListLayoutBinding,
        private val adapter: AccountListAdapter
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Account) {
            binding.accountTypeTextView.text = item.account_type
            binding.accountNumberTextView.text = item.account_id.toString()
            binding.accountBalanceTextView.text = context?.resources?.getString(
                R.string.acc_balance,
                item.balance.toString()
            )


            binding.accountCardLayout.setOnClickListener {
                val intent = Intent(binding.accountCardLayout.context, AccountDetailActivity::class.java)
                intent.putExtra("account_id", item.account_id)
                intent.putExtra("customer_id", item.customer_id)
                intent.putExtra("account_type", item.account_type)
                binding.accountCardLayout.context?.startActivity(intent)
            }
        }
    }


    companion object {
        val ACCOUNT_LIST_COMPARATOR = object : DiffUtil.ItemCallback<Account>() {
            override fun areItemsTheSame(
                oldItem: Account,
                newItem: Account
            ): Boolean =
                oldItem.account_id == newItem.account_id

            override fun areContentsTheSame(
                oldItem: Account,
                newItem: Account
            ): Boolean =
                oldItem == newItem
        }
    }
}


