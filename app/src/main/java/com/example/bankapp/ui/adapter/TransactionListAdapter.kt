package com.example.bankapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bankapp.databinding.AccountListLayoutBinding
import com.example.bankapp.databinding.TransactionListItemBinding
import com.example.bankapp.model.tables.Account
import com.example.bankapp.model.tables.Transaction
import com.example.bankapp.ui.view.AccountDetailActivity

class TransactionListAdapter : ListAdapter<Transaction, TransactionListAdapter.TransactionListViewHolder>(TRANSACTION_LIST_COMPARATOR) {

    private var context: Context? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionListAdapter.TransactionListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TransactionListItemBinding.inflate(inflater, parent, false)
        context = parent.context
        return TransactionListViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: TransactionListAdapter.TransactionListViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.bind(transaction)
    }


    inner class TransactionListViewHolder(
        private val binding: TransactionListItemBinding,
        private val adapter: TransactionListAdapter
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Transaction) {
            binding.transactionId.text = item.transactionTime
            binding.transactionType.text = item.transactionType
            binding.transactionAmount.text = item.transactionAmount.toString()
            binding.netBalance.text = item.netBalance.toString()
        }
    }


    companion object {
        val TRANSACTION_LIST_COMPARATOR = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(
                oldItem: Transaction,
                newItem: Transaction
            ): Boolean =
                oldItem.transactionId == newItem.transactionId

            override fun areContentsTheSame(
                oldItem: Transaction,
                newItem: Transaction
            ): Boolean =
                oldItem == newItem
        }
    }
}
