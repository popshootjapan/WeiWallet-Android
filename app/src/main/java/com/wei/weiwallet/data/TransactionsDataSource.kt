package com.wei.weiwallet.data

import com.wei.weiwallet.data.model.Balance
import com.wei.weiwallet.data.model.Params
import com.wei.weiwallet.data.model.TransactionCount
import io.reactivex.Observable

/**
 * Created by enkaism on 2018/04/28.
 */
interface TransactionsDataSource {
  fun sendRawTransaction(params: Params<String>): Observable<String>
  fun getTransactionCount(params: Params<TransactionCount>): Observable<Int>
  fun getBalance(params: Params<Balance>): Observable<String>
}
