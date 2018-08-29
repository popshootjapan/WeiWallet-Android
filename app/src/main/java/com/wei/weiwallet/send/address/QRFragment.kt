package com.wei.weiwallet.send.address

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.wei.wei.ext.isValidAddress
import com.wei.weiwallet.R
import com.wei.weiwallet.databinding.FragmentQrBinding
import com.wei.weiwallet.send.amount.AmountActivity
import dagger.android.support.DaggerFragment
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import javax.inject.Inject

@RuntimePermissions
class QRFragment : DaggerFragment() {

  @Inject
  lateinit var viewModel: AddressViewModel

  private lateinit var binding: FragmentQrBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? = DataBindingUtil.inflate<FragmentQrBinding>(
      inflater, R.layout.fragment_qr, container, false).also {
    binding = it
  }.root

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupBarcodeViewWithPermissionCheck()
  }

  @NeedsPermission(Manifest.permission.CAMERA)
  fun setupBarcodeView() {
    binding.barcodeView.decodeSingle(object : BarcodeCallback {
      override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
      }

      override fun barcodeResult(result: BarcodeResult?) {
        val address = result.toString()
        if (address.isValidAddress()) {
          val intent = Intent(activity, AmountActivity::class.java)
          intent.putExtra("address", address)
          activity.startActivityForResult(intent, AddressActivity.REQUEST_CODE)
        } else {
          view?.let {
            Snackbar.make(it, R.string.invalid_address, Snackbar.LENGTH_LONG).show()
          }
        }
      }
    })
  }

  @SuppressLint("NeedOnRequestPermissionsResult")
  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
      grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    onRequestPermissionsResult(requestCode, grantResults)
  }

  override fun onResume() {
    super.onResume()
    binding.barcodeView.resume()
  }

  override fun onPause() {
    super.onPause()
    binding.barcodeView.pause()
  }
}
