package com.wei.weiwallet.data

import com.wei.weiwallet.data.model.EtherscanResponse
import io.reactivex.Observable

interface EtherscanDataSource {
  fun getTransactionList(apiKey: String, address: String): Observable<EtherscanResponse>
}
