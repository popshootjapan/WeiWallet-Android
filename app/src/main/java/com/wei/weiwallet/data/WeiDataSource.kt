package com.wei.weiwallet.data

import com.wei.weiwallet.data.model.*
import io.reactivex.Observable

/**
 * Created by enkaism on 2018/05/15.
 */
interface WeiDataSource {
  fun status(token: String): Observable<Status>
  fun currentRate(currency: Fiat): Observable<CurrentRate>
  fun convertEth(wei: String, currency: Fiat): Observable<Convert>
  fun convertFiat(amount: String, currency: Fiat): Observable<Convert>
  fun sign(body: SignBody): Observable<JWTToken>
  fun device(body: JWTToken): Observable<Response>
  fun agreementVersion(token: String): Observable<Response>
}
