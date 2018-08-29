package com.wei.weiwallet.transaction_list

import androidx.lifecycle.ViewModel
import com.wei.weiwallet.data.EtherscanRepository
import com.wei.weiwallet.data.SecretRepository
import com.wei.weiwallet.data.model.EtherscanResponse
import com.wei.weiwallet.di.DataModule
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class TransactionListViewModel @Inject constructor(
        private val repository: EtherscanRepository,
        private val secretRepository: SecretRepository
) : ViewModel() {
    val transactionProcessor: PublishSubject<Unit> = PublishSubject.create<Unit>()

    fun getTransactionList(): Observable<EtherscanResponse> =
            repository.getTransactionList(DataModule.etherScanApiKey, getMyAddress())

    fun getMyAddress(): String = secretRepository.address()
}
