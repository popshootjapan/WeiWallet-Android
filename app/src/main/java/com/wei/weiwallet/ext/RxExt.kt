package com.wei.weiwallet.ext

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.wei.weiwallet.data.model.Result
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException

fun <T> Observable<T>.background(): Observable<T> = subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.toResult(): Observable<Result<T>> {
  return this.map { it.asResult() }
      .onErrorReturn {
        if (it is HttpException || it is IOException) {
          return@onErrorReturn it.asErrorResult<T>()
        } else {
          throw it
        }
      }
}

fun <T> T.asResult(): Result<T> {
  return Result(data = this, error = null)
}

fun <T> Throwable.asErrorResult(): Result<T> {
  return Result(data = null, error = this)
}
