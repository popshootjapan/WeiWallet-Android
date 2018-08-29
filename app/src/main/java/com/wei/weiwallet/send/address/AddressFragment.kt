package com.wei.weiwallet.send.address

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wei.wei.ext.isValidAddress
import com.wei.weiwallet.R
import com.wei.weiwallet.databinding.FragmentAddressBinding
import com.wei.weiwallet.send.amount.AmountActivity
import com.wei.weiwallet.util.KeyboardUtil
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class AddressFragment : DaggerFragment() {
  @Inject
  lateinit var viewModel: AddressViewModel

  private lateinit var binding: FragmentAddressBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? = DataBindingUtil.inflate<FragmentAddressBinding>(
      inflater, R.layout.fragment_address, container, false).also {
    binding = it
  }.root

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.pasteButton.setOnClickListener {
      val address = KeyboardUtil.paste(context)
      binding.address.text = address
      if (address.isValidAddress()) {
        val intent = Intent(activity, AmountActivity::class.java)
        intent.putExtra("address", address)
        activity.startActivityForResult(intent, AddressActivity.REQUEST_CODE)
      } else {
        Snackbar.make(it, R.string.invalid_address, Snackbar.LENGTH_LONG).show()
      }
    }
  }
}
