package com.wei.weiwallet.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.MenuItem
import com.wei.weiwallet.R
import com.wei.weiwallet.backup.BackupFragment
import com.wei.weiwallet.ext.background
import com.wei.weiwallet.ext.toResult
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        supportActionBar?.setTitle(R.string.my_wallet)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, MainFragment())
        transaction.commit()

        viewModel.getStatus()
                .background()
                .doOnError {
                    showStatusError(it.localizedMessage)
                }
                .retry(10)
                .subscribe {
                    when {
                        it.maintenance_ongoing -> {
                            Log.d("debug", "maintenance_ongoing")
                            showMaintenance()
                            return@subscribe
                        }
                        it.need_update -> {
                            Log.d("debug", "need_update")
                            openAppStore()
                            return@subscribe
                        }
                        it.need_terms_agreement -> {
                            Log.d("debug", "need_terms_agreement")
                            confirmTerms()
                            return@subscribe
                        }
                    }
                }

        viewModel.agreementVersionProcessor
                .flatMap { viewModel.putAgreementVersion().background() }
                .toResult()
                .retry(10)
                .subscribe({
                    it.data?.let {
                        if (it.ok) {
                            Log.d("debug", "Put success agreement api")
                        } else {
                            // TODO: Crashlytics Send Error
                        }
                    }

                    it.error?.let {
                        AlertDialog.Builder(this)
                                .setMessage(R.string.network_error)
                                .setPositiveButton(R.string.ok, { _, _ ->
                                })
                                .show()
                    }
                }, {
                    Log.d("debug", "error")
                })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_wallet -> {
                supportActionBar?.setTitle(R.string.my_wallet)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, MainFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }
            R.id.nav_backup -> {
                supportActionBar?.setTitle(R.string.backup)
                val transaction = supportFragmentManager.beginTransaction()

                transaction.replace(R.id.container, BackupFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }
            R.id.nav_terms -> {
                val uri = Uri.parse(getString(R.string.terms_url))
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
            R.id.nav_policy -> {
                val uri = Uri.parse(getString(R.string.policy_url))
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showStatusError(message: String) {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.error_title))
                .setMessage(message)
                .show()
    }

    private fun openAppStore() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.store_title))
        builder.setMessage(getString(R.string.store_message))
        builder.setPositiveButton(getString(R.string.update_title), null)
        val dialog = builder.show()
        dialog.setCancelable(false)
        val button = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        button.setOnClickListener {
            val uri = Uri.parse(getString(R.string.play_store_url))
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    private fun showMaintenance() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.maintenance_title))
        builder.setMessage(getString(R.string.maintenance_message))
        val dialog = builder.show()
        dialog.setCancelable(false)
    }

    private fun confirmTerms() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.terms_title))
        builder.setMessage(getString(R.string.terms_message))
        builder.setPositiveButton(getString(R.string.confirm_terms), null)
        builder.setNegativeButton(getString(R.string.terms), null)
        val dialog = builder.show()
        dialog.setCancelable(false)
        val button = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        button.setOnClickListener {
            viewModel.agreementVersionProcessor.onNext(Unit)
            dialog.dismiss()
        }
        val termsButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        termsButton.setOnClickListener {
            val uri = Uri.parse(getString(R.string.terms_url))
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }
}
