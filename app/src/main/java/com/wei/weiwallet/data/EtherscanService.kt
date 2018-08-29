package com.wei.weiwallet.data

import com.wei.weiwallet.data.model.EtherscanResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface EtherscanService {
  @GET("/api")
  fun getTransactionList(
    @Query("apiKey") apiKey: String, @Query("module") account: String, @Query(
        "action"
    ) action: String, @Query("address") address: String, @Query(
        "startblock"
    ) startblock: Int, @Query("endblock") endblock: Int,
    @Query("sort") sort: String
  ): Observable<EtherscanResponse>
}
