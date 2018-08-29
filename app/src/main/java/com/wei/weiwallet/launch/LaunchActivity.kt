package com.wei.weiwallet.launch

import android.app.AlertDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.crashlytics.android.Crashlytics
import com.jakewharton.rxbinding2.view.clicks
import com.wei.weiwallet.R
import com.wei.weiwallet.databinding.ActivityLaunchBinding
import com.wei.weiwallet.ext.background
import com.wei.weiwallet.main.MainActivity
import com.wei.weiwallet.restoration.RestorationActivity
import dagger.android.support.DaggerAppCompatActivity
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_launch.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LaunchActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModel: LaunchViewModel
    private lateinit var binding: ActivityLaunchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        binding = DataBindingUtil.setContentView(this, R.layout.activity_launch)

        viewModel.addLaunchCount()

//        if (viewModel.isSigned()) {
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }

        launch_button.clicks()
                .throttleFirst(1, TimeUnit.SECONDS)
                .flatMap {
                    progress.visibility = View.VISIBLE
                    viewModel.sign().background()
                }
                .doOnError {
                    progress.visibility = View.GONE
                    AlertDialog.Builder(this)
                            .setMessage(R.string.launch_failed)
                            .setPositiveButton(R.string.launch_failed_button, { _, _ ->
                            })
                            .show()
                }
                .retry()
                .subscribe {
                    progress.visibility = View.GONE
                    viewModel.putToken(it.token)
                    startActivity(Intent(this, MainActivity::class.java))
                }

        restoration_button.setOnClickListener {
            startActivity(Intent(this, RestorationActivity::class.java))
        }
        privacy_policy_button.setOnClickListener {
            val uri = Uri.parse(getString(R.string.policy_url))
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        terms_button.setOnClickListener {
            val uri = Uri.parse(getString(R.string.terms_url))
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }
}
