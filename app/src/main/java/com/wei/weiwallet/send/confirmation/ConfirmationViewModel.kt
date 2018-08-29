package com.wei.weiwallet.send.confirmation

import androidx.lifecycle.ViewModel
import com.wei.wei.crypto.EIP155Signer
import com.wei.wei.crypto.publicKeyFromPrivate
import com.wei.wei.ext.hexToByteArray
import com.wei.wei.ext.toHexString
import com.wei.wei.model.ECKeyPair
import com.wei.wei.model.SignTransaction
import com.wei.weiwallet.data.SecretRepository
import com.wei.weiwallet.data.TransactionsRepository
import com.wei.weiwallet.data.model.BlockParameter
import com.wei.weiwallet.data.model.Params
import com.wei.weiwallet.data.model.TransactionCount
import com.wei.weiwallet.di.DataModule
import com.wei.weiwallet.ext.toWei
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject

class ConfirmationViewModel @Inject constructor(
        private val repository: TransactionsRepository,
        private val secretRepository: SecretRepository
) : ViewModel() {
    val transactionSubject = PublishSubject.create<String>()

    fun getTransactionCount(address: String): Observable<Int> {
        return repository.getTransactionCount(Params(
                TransactionCount("0x$address", BlockParameter.PENDING)))
    }

    fun sendTransaction(privateKey: BigInteger, value: String, to: String,
                        nonce: Int): Observable<String> {
        val latestNonce = latestNonce(nonce)
        val transaction = EIP155Signer.sign(
                SignTransaction(BigDecimal.valueOf(value.toDouble().toWei()).toBigInteger(),
                        to.hexToByteArray(), latestNonce, BigInteger("41000000000"),
                        DataModule.defaultGasLimit), ECKeyPair(privateKey, publicKeyFromPrivate(privateKey)),
                DataModule.chainId).toHexString("0x")
        return repository.sendRawTransaction(Params(transaction))
    }

    private fun latestNonce(nonce: Int): Int {
        val cachedNonce = secretRepository.nonce()
        return if (cachedNonce > nonce) cachedNonce else nonce
    }
}
