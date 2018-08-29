package com.wei.weiwallet.receive

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.wei.weiwallet.R
import com.wei.weiwallet.databinding.ActivityReceiveBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * Created by enkaism on 2018/05/22.
 */
class ReceiveActivity : DaggerAppCompatActivity() {
  @Inject
  lateinit var viewModel: ReceiveViewModel
  private lateinit var binding: ActivityReceiveBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_receive)
    setQRImageView()
    supportActionBar?.title = getString(R.string.receive)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    binding.addressButton.setOnClickListener {
      copyAddressToClipboard()
    }

    binding.addressTextView.text = viewModel.getMyAddress()
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
    android.R.id.home -> {
      finish()
      true
    }
    else -> super.onOptionsItemSelected(item)
  }

  private fun setQRImageView() {
    try {
      val barcodeEncoder = BarcodeEncoder()
      val bitmap =
        barcodeEncoder.encodeBitmap(viewModel.getMyAddress(), BarcodeFormat.QR_CODE, 220, 220)

      val imageViewQrCode = findViewById<ImageView>(R.id.image_view)
      imageViewQrCode.setImageBitmap(bitmap)
    } catch (e: Exception) {
      AlertDialog.Builder(this)
          .setTitle(R.string.error)
          .setMessage(e.localizedMessage)
          .setPositiveButton(R.string.ok, { _, _ ->
            // TODO: Retry?
          })
          .show()
    }
  }

  // TODO: to use KeyboardUtil
  private fun copyAddressToClipboard() {
    val item = ClipData.Item(viewModel.getMyAddress())
    val mimeType = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
    val cd = ClipData(ClipDescription("text_data", mimeType), item)
    val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    cm.primaryClip = cd
    Toast.makeText(this, R.string.copied, Toast.LENGTH_SHORT)
        .show()
  }
}
