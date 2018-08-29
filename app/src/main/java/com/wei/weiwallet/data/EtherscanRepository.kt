package com.wei.weiwallet.data

import com.wei.weiwallet.data.model.EtherscanResponse
import io.reactivex.Observable
import javax.inject.Inject

class EtherscanRepository @Inject constructor(private val service: EtherscanService) :EtherscanDataSource {
  // TODO: Pagination page=1&offset=10
  override fun getTransactionList(apiKey: String, address: String): Observable<EtherscanResponse>
      = service.getTransactionList(apiKey, "account", "txlist", address, 0, 99999999, "desc")
}
