package com.wei.weiwallet.data

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.wei.weiwallet.data.model.*
import com.wei.weiwallet.di.DataModule
import io.reactivex.Observable
import okhttp3.*
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by enkaism on 2018/04/28.
 */
@Singleton
class TransactionsRepository @Inject constructor(
        @Named("others")
        private val client: OkHttpClient
) : TransactionsDataSource {
    override fun getTransactionCount(params: Params<TransactionCount>): Observable<Int> {
        val moshi = Moshi.Builder()
                .build()
        val jsonAdapter = moshi.adapter(JsonRPCBody::class.java)

        val body = RequestBody.create(
                MediaType.parse("application/json-rpc"),
                jsonAdapter.toJson(
                        JsonRPCBody(
                                method = "eth_getTransactionCount",
                                params = arrayOf(params.params.address, params.params.blockParameter.value)
                        )
                )
        )
        val request = Request.Builder().url(DataModule.infuraUrl).post(body).build()

        return Observable.create<Int> {
            client.newCall(request)
                    .enqueue(object : Callback {
                        override fun onFailure(
                                call: Call?,
                                e: IOException?
                        ) {
                            it.onError(Throwable(e))
                        }

                        override fun onResponse(
                                call: Call?,
                                response: Response?
                        ) {
                            val types =
                                    Types.newParameterizedType(JsonRPCResponse::class.java, String::class.java)
                            val adapter = moshi.adapter<JsonRPCResponse<String>>(types)
                            val result = response?.body()
                                    ?.string()
                            Log.d("log", result)
                            it.onNext(adapter.fromJson(result)!!.result.removePrefix("0x").toInt(16))
                        }
                    })
        }
    }

    override fun sendRawTransaction(params: Params<String>): Observable<String> {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(JsonRPCBody::class.java)

        val body = RequestBody.create(MediaType.parse("application/json-rpc"),
                jsonAdapter.toJson(JsonRPCBody(method = "eth_sendRawTransaction",
                        params = arrayOf(params.params))))
        val request = Request.Builder().url(DataModule.infuraUrl).post(body).build()
        return Observable.create<String> {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    it.onError(Throwable(e))
                }

                override fun onResponse(call: Call?, response: Response?) {
                    val types = Types.newParameterizedType(JsonRPCResponse::class.java, String::class.java)
                    val adapter = moshi.adapter<JsonRPCResponse<String>>(types)
                    val result = response?.body()?.string()
                    Log.d("log", result)
                    it.onNext(adapter.fromJson(result)!!.result)
                }
            })
        }
    }

    override fun getBalance(params: Params<Balance>): Observable<String> {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(JsonRPCBody::class.java)

        val body = RequestBody.create(MediaType.parse("application/json-rpc"),
                jsonAdapter.toJson(JsonRPCBody(method = "eth_getBalance",
                        params = arrayOf(params.params.address, params.params.blockParameter.value))))
        val request = Request.Builder().url(
                DataModule.infuraUrl).post(body).build()
        return Observable.create<String> {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    it.onError(Throwable(e))
                }

                override fun onResponse(call: Call?, response: Response?) {
                    val types = Types.newParameterizedType(JsonRPCResponse::class.java, String::class.java)
                    val adapter = moshi.adapter<JsonRPCResponse<String>>(types)
                    val result = response?.body()?.string()
                    Log.d("log", result)
                    it.onNext(adapter.fromJson(result)!!.result)
                }
            })
        }
    }
}

