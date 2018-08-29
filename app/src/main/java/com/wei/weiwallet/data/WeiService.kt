package com.wei.weiwallet.data

import com.wei.weiwallet.data.model.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by enkaism on 2018/05/11.
 */
interface WeiService {
  @GET("/api/v2/status")
  fun status(@Header("Authorization") token: String): Observable<Status>

  @GET("/api/v2/eth/rate")
  fun currentRate(@Query("currency") currency: Fiat): Observable<CurrentRate>

  @GET("/api/v2/eth/convert")
  fun convertEth(@Query("wei") wei: String, @Query("currency") currency: Fiat): Observable<Convert>

  @GET("/api/v2/fiat/convert")
  fun convertFiat(@Query("amount") amount: String, @Query(
      "currency") currency: Fiat): Observable<Convert>

  @PUT("/api/v2/sign")
  fun sign(@Body body: SignBody): Observable<JWTToken>

  @PUT("/api/v2/device")
  fun device(@Body body: JWTToken): Observable<Response>

  @PUT("/api/v2/agreement_version")
  fun agreementVersion(@Header("Authorization") token: String): Observable<Response>
}
