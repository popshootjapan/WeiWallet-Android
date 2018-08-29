package com.wei.weiwallet.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.wei.weiwallet.data.EtherscanRepository
import com.wei.weiwallet.data.SecretRepository
import com.wei.weiwallet.data.TransactionsRepository
import com.wei.weiwallet.data.WeiRepository
import com.wei.weiwallet.data.model.*
import com.wei.weiwallet.di.DataModule
import com.wei.weiwallet.ext.background
import com.wei.weiwallet.ext.convertEther
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by enkaism on 2018/05/09.
 */
class MainViewModel @Inject constructor(
        private val repository: TransactionsRepository,
        private val weiRepository: WeiRepository,
        private val etherscanRepository: EtherscanRepository,
        private val sharedPreferences: SharedPreferences,
        private val secretRepository: SecretRepository
) : ViewModel() {
    val transactionProcessor: PublishSubject<Unit> = PublishSubject.create<Unit>()
    val balanceProcessor: PublishSubject<Unit> = PublishSubject.create<Unit>()
    val agreementVersionProcessor: PublishSubject<Unit> = PublishSubject.create<Unit>()

    fun getTransactionList(): Observable<EtherscanResponse> = etherscanRepository.getTransactionList(
            DataModule.etherScanApiKey, getMyAddress()
    )

    fun getMyAddress(): String = "0x" + sharedPreferences.getString("address", "")

    fun getCurrentRate() = weiRepository.currentRate(Fiat.JPY).background()
    fun getBalance() = repository.getBalance(
            Params(
                    Balance(getMyAddress(),
                            BlockParameter.PENDING))).background()

    fun calculateEtherBalance(balanceHex: String): Double =
            balanceHex.removePrefix("0x").toLong(
                    16).toDouble().convertEther()

    fun isFirstLaunch(): Boolean = secretRepository.launchCount() == 1
    fun isShownOnBoarding(): Boolean = secretRepository.isShownOnBoarding()
    fun isAlreadyBackup(): Boolean = secretRepository.isAlreadyBackup()

    fun getStatus(): Observable<Status> = weiRepository.status(secretRepository.token())
    fun putAgreementVersion(): Observable<Response> = weiRepository.agreementVersion(secretRepository.token())
}
