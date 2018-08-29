package com.wei.weiwallet.send.address

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.MenuItem
import com.wei.weiwallet.R
import com.wei.weiwallet.databinding.ActivityAddressBinding
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_address.*
import javax.inject.Inject

/**
 * Created by enkaism on 2018/05/22.
 */
class AddressActivity : DaggerAppCompatActivity() {
  companion object {
    const val REQUEST_CODE = 10001
  }

  @Inject
  lateinit var viewModel: AddressViewModel

  private lateinit var binding: ActivityAddressBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    supportActionBar?.title = getString(R.string.select_address)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_address)

    val adapter = AddressFragmentPagerAdapter(supportFragmentManager, this)
    view_pager.offscreenPageLimit = 2
    view_pager.adapter = adapter

    tab_layout.setupWithViewPager(view_pager)
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
    if (requestCode == AddressActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) finish()
  }
}
