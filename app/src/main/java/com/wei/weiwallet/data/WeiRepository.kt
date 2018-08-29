package com.wei.weiwallet.data

import com.wei.weiwallet.data.model.*
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by enkaism on 2018/05/15.
 */
class WeiRepository @Inject constructor(private val service: WeiService) : WeiDataSource {
  override fun status(token: String): Observable<Status> = service.status(token)

  override fun currentRate(currency: Fiat): Observable<CurrentRate> = service.currentRate(currency)

  override fun convertEth(wei: String, currency: Fiat): Observable<Convert> = service.convertEth(
      wei, currency)

  override fun convertFiat(amount: String,
      currency: Fiat): Observable<Convert> = service.convertFiat(amount, currency)

  override fun sign(body: SignBody): Observable<JWTToken> = service.sign(body)

  override fun device(body: JWTToken): Observable<Response> = service.device(body)

  override fun agreementVersion(token: String): Observable<Response> = service.agreementVersion(token)
}
