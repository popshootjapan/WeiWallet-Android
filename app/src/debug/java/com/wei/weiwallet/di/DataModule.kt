package com.wei.weiwallet.di

import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.wei.weiwallet.App
import com.wei.weiwallet.data.EtherscanService
import com.wei.weiwallet.data.WeiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.math.BigInteger
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by enkaism on 2018/05/09.
 */
@Module
class DataModule {

    companion object {
        const val infuraUrl = "https://ropsten.infura.io/z1sEfnzz0LLMsdYMX4PV"
        const val etherScanApiKey = "XE7QVJNVMKJT75ATEPY1HPWTPYCVCKMMJ7"
        const val chainId = 3
        var defaultGasLimit = BigInteger("21000")
    }

    @Singleton
    @Provides
    fun sharedPreferences(app: App) = app.getSharedPreferences("keys", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun moshi() = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Singleton
    @Provides
    @Named("wei")
    fun provideOkHttpWei(): OkHttpClient = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .addInterceptor({
                val original = it.request()

                val request = original.newBuilder()
                        .header("os", "android")
                        .header("version", "1.0.0")
                        .method(original.method(), original.body())
                        .build()

                it.proceed(request)
            }).build()

    @Singleton
    @Provides
    @Named("others")
    fun provideOkHttpOthers(): OkHttpClient = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .addInterceptor({
                val original = it.request()

                val request = original.newBuilder()
                        .method(original.method(), original.body())
                        .build()

                it.proceed(request)
            }).build()


    @Singleton
    @Provides
    @Named("wei")
    fun provideWeiRetrofit(@Named("wei") client: OkHttpClient,
                           moshi: Moshi): Retrofit = Retrofit.Builder().client(client).baseUrl(
            "https://stg.wei.tokyo/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()


    @Singleton
    @Provides
    @Named("others")
    fun provideEtherscanRetrofit(@Named("others") client: OkHttpClient,
                                 moshi: Moshi): Retrofit = Retrofit.Builder().client(client).baseUrl(
            "https://ropsten.etherscan.io")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideWeiService(@Named("wei") retrofit: Retrofit) = retrofit.create(WeiService::class.java)

    @Singleton
    @Provides
    fun provideEtherscanService(@Named("others") retrofit: Retrofit) = retrofit.create(
            EtherscanService::class.java)


}
