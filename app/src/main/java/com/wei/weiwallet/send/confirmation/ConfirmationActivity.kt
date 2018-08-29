package com.wei.weiwallet.send.confirmation

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import com.wei.weiwallet.R
import com.wei.weiwallet.ext.background
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_confirmation.*
import java.math.BigInteger
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConfirmationActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModel: ConfirmationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
        supportActionBar?.title = getString(R.string.confirm_send)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        address.text = intent.getStringExtra("address")
        amount.text = intent.getStringExtra("amount")
        amount_eth.text = getString(R.string.amount_eth, intent.getStringExtra("amount_eth"))
        fee.text = getString(R.string.formatted_yen, intent.getStringExtra("fee"))
        fee_eth.text = getString(R.string.amount_eth, intent.getStringExtra("fee_eth"))

        val sharedPreferences = getSharedPreferences("keys", Context.MODE_PRIVATE)
        val privateKey = sharedPreferences.getString("private_key", "")

        progress.visibility = View.VISIBLE
        viewModel.transactionSubject
                .flatMap { viewModel.getTransactionCount(it) }
                .flatMap {
                    viewModel.sendTransaction(BigInteger(privateKey), intent.getStringExtra("amount_eth"),
                            intent.getStringExtra("address"), it)
                }
                .background()
                .doOnError {
                    progress.visibility = View.GONE
                    AlertDialog.Builder(this)
                            .setMessage(it.message!!)
                            .setPositiveButton(it.localizedMessage, { _, _ ->
                            })
                            .show()
                }
                .retry()
                .subscribe {
                    progress.visibility = View.GONE
                    AlertDialog.Builder(this)
                            .setMessage(R.string.finish_send)
                            .setPositiveButton(R.string.ok, { _, _ ->
                                setResult(Activity.RESULT_OK)
                                finish()
                            })
                            .show()
                }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.send, menu)
        val item = menu?.findItem(R.id.action_decision)
        item?.setActionView(R.layout.view_decision)
        item?.actionView?.clicks()?.throttleFirst(1, TimeUnit.SECONDS)?.subscribe {
            val sharedPreferences = getSharedPreferences("keys", Context.MODE_PRIVATE)
            val address = sharedPreferences.getString("address", "")

            viewModel.transactionSubject.onNext(address)
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
}
