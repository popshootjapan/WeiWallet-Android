package com.wei.weiwallet.transaction_list

import android.app.AlertDialog
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.wei.weiwallet.R
import com.wei.weiwallet.R.string
import com.wei.weiwallet.data.model.EtherscanTransaction
import com.wei.weiwallet.databinding.ActivityTransactionListBinding
import com.wei.weiwallet.databinding.TransactionItemBinding
import com.wei.weiwallet.ext.background
import com.wei.weiwallet.ext.toResult
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_transaction_list.*
import javax.inject.Inject

class TransactionListActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModel: TransactionListViewModel
    private lateinit var binding: ActivityTransactionListBinding
    private lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_list)
        adapter = TransactionAdapter(viewModel.getMyAddress())

        supportActionBar?.title = getString(string.transaction_list_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.run {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@TransactionListActivity)
        }

        progress.visibility = View.VISIBLE
        viewModel.transactionProcessor
                .flatMap { viewModel.getTransactionList().background() }
                .toResult()
                .subscribe({
                    it.data?.let {
                        progress.visibility = View.GONE
                        Log.d("debug", it.message)
                        text_view.visibility = if (it.result.count() == 0) View.VISIBLE else View.GONE
                        binding.refresh.isRefreshing = false

                        if (it.message == getString(string.ok)) {
                            adapter.run {
                                items.clear()
                                items.addAll(it.result)
                                notifyDataSetChanged()
                            }
                        }
                    }

                    it.error?.let {
                        progress.visibility = View.GONE
                        AlertDialog.Builder(this)
                                .setMessage(R.string.network_error)
                                .setPositiveButton(R.string.ok, { _, _ ->
                                })
                                .show()
                    }
                }, {
                    Log.d("debug", it.localizedMessage)
                })

        binding.refresh.isRefreshing = true
        viewModel.transactionProcessor.onNext(Unit)

        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = true
            viewModel.transactionProcessor.onNext(Unit)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}

class TransactionAdapter(address: String) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    val items = ArrayList<EtherscanTransaction>()
    val address = address

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): TransactionAdapter.ViewHolder {
        return ViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.transaction_item, parent, false
                )
        )
    }

    override fun onBindViewHolder(
            holder: TransactionAdapter.ViewHolder,
            position: Int
    ) {
        holder.binding.transaction = items[position]
        holder.binding.myAddress = address

        if (items[position].isReceiveTransaction(address)) {
            setImageResource(holder.binding.thumbnail, R.drawable.ic_receive)
        } else {
            setImageResource(holder.binding.thumbnail, R.drawable.ic_send)
        }
    }

    @BindingAdapter("imageResource")
    fun setImageResource(
            imageView: ImageView,
            resource: Int
    ) {
        imageView.setImageResource(resource)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(val binding: TransactionItemBinding) : RecyclerView.ViewHolder(
            binding.root
    )
}