package com.wei.weiwallet.receive

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.wei.weiwallet.data.SecretRepository
import javax.inject.Inject

/**
 * Created by enkaism on 2018/05/22.
 */
class ReceiveViewModel @Inject constructor(
  private val secretRepository: SecretRepository
) : ViewModel() {
  fun getMyAddress() = secretRepository.address()
}
