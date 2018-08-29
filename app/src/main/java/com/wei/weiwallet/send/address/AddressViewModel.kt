package com.wei.weiwallet.send.address

import androidx.lifecycle.ViewModel
import com.wei.weiwallet.data.TransactionsRepository
import com.wei.weiwallet.data.WeiRepository
import javax.inject.Inject

/**
 * Created by enkaism on 2018/05/22.
 */
class AddressViewModel @Inject constructor(
        private val repository: TransactionsRepository,
        private val weiRepository: WeiRepository
) : ViewModel() {

}
