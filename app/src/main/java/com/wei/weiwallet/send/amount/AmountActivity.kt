package com.wei.weiwallet.send.amount

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.jakewharton.rxbinding2.widget.textChanges
import com.wei.weiwallet.R
import com.wei.weiwallet.data.model.CurrentRate
import com.wei.weiwallet.databinding.ActivityAmountBinding
import com.wei.weiwallet.send.confirmation.ConfirmationActivity
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import io.reactivex.functions.Function3
import kotlinx.android.synthetic.main.activity_amount.*
import javax.inject.Inject

/**
 * Created by enkaism on 2018/05/22.
 */
class AmountActivity : DaggerAppCompatActivity() {
    companion object {
        const val REQUEST_CODE = 10002
    }

    @Inject
    lateinit var viewModel: AmountViewModel

    private lateinit var binding: ActivityAmountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_amount)
        supportActionBar?.title = getString(R.string.input_amount)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progress.visibility = View.VISIBLE
        Observable.combineLatest(amount.textChanges(),
                viewModel.getAmount(),
                viewModel.getBalance(),
                Function3 { t1: CharSequence, t2: CurrentRate, t3: String ->
                    Triple(t1, t2, t3)
                })
                .subscribe({
                    progress.visibility = View.GONE
                    val amountEth = viewModel.calculateAmountEth(it.first.toString(), it.second.price)
                    val availableAmount = viewModel.calculateAvailableAmount(it.third, it.second.price)
                    val fee = viewModel.calculateFee(it.second.price)

                    if (it.first.isNotEmpty() && it.first.toString().toInt() > availableAmount.toDouble().toInt() - fee) {
                        if (it.first.toString().toInt() == 0) return@subscribe
                        amount.setText("${Math.max(availableAmount.toDouble().toInt() - fee, 0)}")
                        return@subscribe
                    }

                    viewModel.setConfirmationData(amountEth, fee.toString(), viewModel.calculateFeeEth())
                    amount_eth.text = getString(R.string.amount_eth, amountEth)
                    available_amount.text = "${Math.max(availableAmount.toDouble().toInt() - fee, 0)}"
                    available_amount_description.text = getString(R.string.available_amount_description,
                            availableAmount.toDouble().toInt().toString(), fee.toString())
                }, {
                    progress.visibility = View.GONE
                    Log.d("log", it.message)
                })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.send, menu)
        val item = menu?.findItem(R.id.action_decision)
        item?.setActionView(R.layout.view_decision)
        item?.actionView?.setOnClickListener {
            if (amount.text.isEmpty() || amount.text.toString() == "0") {
                Snackbar.make(findViewById(android.R.id.content), R.string.amount_is_zero,
                        Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val intent = Intent(this, ConfirmationActivity::class.java)
            intent.putExtra("amount", binding.amount.text.toString())
            intent.putExtra("fee", viewModel.fee)
            intent.putExtra("amount_eth", viewModel.amountEth)
            intent.putExtra("fee_eth", viewModel.feeEth)
            intent.putExtra("address", getIntent().getStringExtra("address"))
            startActivityForResult(intent, REQUEST_CODE)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
