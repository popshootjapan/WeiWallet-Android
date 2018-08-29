package com.wei.weiwallet.restoration

import android.app.AlertDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.jakewharton.rxbinding2.view.clicks
import com.wei.weiwallet.R
import com.wei.weiwallet.databinding.ActivityRestorationBinding
import com.wei.weiwallet.ext.background
import com.wei.weiwallet.main.MainActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_restoration.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RestorationActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModel: RestorationViewModel
    private lateinit var binding: ActivityRestorationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restoration)

        supportActionBar?.title = getString(R.string.restoration)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.restoreSubject.flatMap {
            viewModel.restore(it).background()
        }
                .retry(10)
                .doOnError {
                    progress.visibility = View.GONE
                    AlertDialog.Builder(this)
                            .setMessage(R.string.launch_failed)
                            .setPositiveButton(R.string.launch_failed_button, { _, _ ->
                            })
                            .show()
                }
                .subscribe {
                    progress.visibility = View.GONE
                    viewModel.putToken(it.token)
                    startActivity(Intent(this, MainActivity::class.java))
                }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.confirm, menu)
        val item = menu?.findItem(R.id.action_confirm)
        item?.setActionView(R.layout.view_confirm)
        item?.actionView?.clicks()?.throttleFirst(1, TimeUnit.SECONDS)?.subscribe {
            // TODO: DRY
            val m1 = (0 until left.childCount)
                    .map { left.getChildAt(it) }
                    .filter { it is EditText }
                    .map { (it as EditText).text.toString() }
                    .reduce { s1, s2 -> "$s1 $s2" }
            val m2 = (0 until right.childCount)
                    .map { right.getChildAt(it) }
                    .filter { it is EditText }
                    .map { (it as EditText).text.toString() }
                    .reduce { s1, s2 -> "$s1 $s2" }
            val mnemonic = "$m1 $m2"
            if (viewModel.isValidMnemonic(mnemonic)) {
                progress.visibility = View.VISIBLE
                viewModel.restoreSubject.onNext(mnemonic)
            } else {
                AlertDialog.Builder(this)
                        .setMessage(R.string.validation_mnemonic)
                        .setPositiveButton(R.string.ok, { _, _ ->
                        })
                        .show()
            }
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
