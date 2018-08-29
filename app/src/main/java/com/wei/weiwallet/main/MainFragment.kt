package com.wei.weiwallet.main

import android.app.AlertDialog
import android.content.Intent
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.wei.weiwallet.R
import com.wei.weiwallet.backup.BackupDialog
import com.wei.weiwallet.backup.BackupFragment
import com.wei.weiwallet.data.model.CurrentRate
import com.wei.weiwallet.data.model.EtherscanTransaction
import com.wei.weiwallet.databinding.FragmentMainBinding
import com.wei.weiwallet.databinding.LatestTransactionItemBinding
import com.wei.weiwallet.ext.background
import com.wei.weiwallet.ext.toResult
import com.wei.weiwallet.ext.toStringWithSeparator
import com.wei.weiwallet.onboarding.OnBoardingDialog
import com.wei.weiwallet.receive.ReceiveActivity
import com.wei.weiwallet.send.address.AddressActivity
import com.wei.weiwallet.transaction_list.TransactionListActivity
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_main.*
import java.math.BigDecimal
import javax.inject.Inject

class MainFragment : DaggerFragment() {

    @Inject
    lateinit var viewModel: MainViewModel

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: LatestTransactionAdapter
    private var dialog: OnBoardingDialog? = null
    private var backupDialog: BackupDialog? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentMainBinding>(
            inflater, R.layout.fragment_main, container, false
    ).also {
        binding = it
    }.root

    override fun onViewCreated(
            view: View?,
            savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        showOnBoardingDialogIfNeeded()

        adapter = LatestTransactionAdapter(viewModel.getMyAddress())

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.isNestedScrollingEnabled = true

        viewModel.transactionProcessor
                .flatMap {
                    progress.visibility = View.VISIBLE
                    viewModel.getTransactionList().background()
                }
                .toResult()
                .subscribe({
                    it.data?.let {
                        progress.visibility = View.GONE
                        Log.d("debug", it.message)
                        if (it.message == getString(R.string.ok)) {
                            adapter.run {
                                items.clear()
                                items.addAll(it.result.filter { it.isExecutedLessThanDay() }.take(2))
                                notifyDataSetChanged()
                            }
                            if (it.result.filter { it.isExecutedLessThanDay() }.count() > 0) {
                                binding.noLatestTransaction.visibility = View.GONE
                            }
                        }
                    }

                    it.error?.let {
                        progress.visibility = View.GONE
                        AlertDialog.Builder(activity)
                                .setMessage(R.string.network_error)
                                .setPositiveButton(R.string.ok, { _, _ ->
                                })
                                .show()
                    }
                }, {
                    progress.visibility = View.GONE
                    Log.d("debug", "error")
                })

        viewModel.balanceProcessor
                .flatMap {
                    progress.visibility = View.VISIBLE
                    Observable.combineLatest(
                            viewModel.getCurrentRate(),
                            viewModel.getBalance(),
                            BiFunction { t1: CurrentRate, t2: String ->
                                Pair(t1, t2)
                            })
                }
                .background()
                .toResult()
                .subscribe({
                    it.data?.let {
                        progress.visibility = View.GONE
                        val etherBalance = viewModel.calculateEtherBalance(it.second)
                        val fiatBalance = BigDecimal(etherBalance * it.first.price.toDouble()).toInt()
                        fiat_balance.text = fiatBalance.toString().toStringWithSeparator()

                        val plainEtherBalance = BigDecimal.valueOf(etherBalance).toPlainString()
                        ether_balance.text = if (plainEtherBalance.length <= 8) plainEtherBalance else plainEtherBalance.substring(0, 8)

                        showBackupDialogIfNeeded(fiatBalance)
                    }
                    it.error?.let {
                        progress.visibility = View.GONE
                        Log.d("debug", it.localizedMessage)
                    }
                }, {
                    progress.visibility = View.GONE
                    ether_balance.text = getString(R.string.hyphen)
                    fiat_balance.text = getString(R.string.hyphen)
                    Log.d("debug", it.localizedMessage)
                })

        transaction_list_button.setOnClickListener {
            startActivity(Intent(activity, TransactionListActivity::class.java))
        }

        receive_button.setOnClickListener {
            startActivity(Intent(activity, ReceiveActivity::class.java))
        }

        button_send.setOnClickListener {
            startActivity(Intent(activity, AddressActivity::class.java))
        }

    }


    override fun onResume() {
        super.onResume()
        viewModel.balanceProcessor.onNext(Unit)
        viewModel.transactionProcessor.onNext(Unit)
    }

    private fun showOnBoardingDialogIfNeeded() {
        if (viewModel.isFirstLaunch() && viewModel.isShownOnBoarding()) {
            dialog = OnBoardingDialog().apply {
                finishText = this@MainFragment.getString(R.string.start)
                finishCallback = this@MainFragment::dismissOnBoardingDialog
            }
            showOnBoardingDialog()
        }
    }

    private fun dismissOnBoardingDialog() {
        dialog?.let { dialog ->
            dialog.dismiss()
            this@MainFragment.dialog = null
        }
    }

    private fun showOnBoardingDialog() {
        dialog?.show(activity.supportFragmentManager, "dialog_fragment")
    }

    private fun showBackupDialogIfNeeded(fiatBalance: Int) {
        if (fiatBalance >= 3000 && !viewModel.isAlreadyBackup()) {
            backupDialog = BackupDialog().apply {
                backupCallback = this@MainFragment::backup
                closeCallback = this@MainFragment::dismissBackupDialog
            }
            backupDialog?.isCancelable = false
            backupDialog?.show(activity.supportFragmentManager, "backup_dialog_fragment")
        }
    }

    private fun dismissBackupDialog() {
        backupDialog?.let { dialog ->
            dialog.dismiss()
            this@MainFragment.backupDialog = null
        }
    }

    private fun backup() {
        dismissBackupDialog()
        val transaction = fragmentManager.beginTransaction()

        transaction.replace(R.id.container, BackupFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

class LatestTransactionAdapter(
        val address: String) : RecyclerView.Adapter<LatestTransactionAdapter.ViewHolder>() {
    val items = ArrayList<EtherscanTransaction>()

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): LatestTransactionAdapter.ViewHolder = ViewHolder(
            DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.latest_transaction_item, parent, false
            )
    )

    override fun onBindViewHolder(
            holder: LatestTransactionAdapter.ViewHolder,
            position: Int
    ) {
        holder.binding.transaction = items[position]
        holder.binding.myAddress = address

        if (items[position].isReceiveTransaction(address)) {
            setImageResource(holder.binding.thumbnail, R.drawable.ic_receive_white)
        } else {
            setImageResource(holder.binding.thumbnail, R.drawable.ic_send_white)
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

    inner class ViewHolder(val binding: LatestTransactionItemBinding) : RecyclerView.ViewHolder(
            binding.root
    )
}
