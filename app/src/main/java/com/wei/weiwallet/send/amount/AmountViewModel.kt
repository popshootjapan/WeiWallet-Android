package com.wei.weiwallet.send.amount

import androidx.lifecycle.ViewModel
import com.wei.wei.model.DEFAULT_GAS_LIMIT
import com.wei.wei.model.DEFAULT_GAS_PRICE
import com.wei.weiwallet.data.SecretRepository
import com.wei.weiwallet.data.TransactionsRepository
import com.wei.weiwallet.data.WeiRepository
import com.wei.weiwallet.data.model.Balance
import com.wei.weiwallet.data.model.BlockParameter
import com.wei.weiwallet.data.model.Fiat
import com.wei.weiwallet.data.model.Params
import com.wei.weiwallet.ext.background
import com.wei.weiwallet.ext.convertEther
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Created by enkaism on 2018/05/22.
 */
class AmountViewModel @Inject constructor(
        private val repository: TransactionsRepository,
        private val weiRepository: WeiRepository,
        private val secretRepository: SecretRepository
) : ViewModel() {
    var amountEth = ""
    var fee = ""
    var feeEth = ""

    fun getAmount() = weiRepository.currentRate(Fiat.JPY).background()
    fun getBalance() = repository.getBalance(
            Params(
                    Balance(secretRepository.address(),
                            BlockParameter.PENDING))).background()

    fun setConfirmationData(amountEth: String, fee: String, feeEth: String) {
        this.amountEth = amountEth
        this.fee = fee
        this.feeEth = feeEth
    }

    fun calculateAmountEth(amount: String, rate: String): String =
            if (amount.isEmpty()) "-" else BigDecimal.valueOf(
                    amount.toDouble() / rate.toDouble()).toPlainString().let {
                if (it.length < 8) it else it.substring(0, 8)
            }

    fun calculateFee(
            rate: String): Int = ((DEFAULT_GAS_LIMIT * DEFAULT_GAS_PRICE).toDouble().convertEther() * rate.toDouble()).toInt()

    fun calculateFeeEth(): String =
            BigDecimal.valueOf(
                    (DEFAULT_GAS_LIMIT * DEFAULT_GAS_PRICE).toDouble().convertEther()).toPlainString()

    fun calculateAvailableAmount(balanceHex: String, rate: String): String =
            BigDecimal.valueOf(balanceHex.removePrefix("0x").toLong(
                    16).toDouble().convertEther() * rate.toDouble()).toPlainString().let {
                if (it.length < 8) it else it.substring(0, 8)
            }
}
