package com.wei.weiwallet.data.model

data class Result<out T>(
    val data: T?,
    val error: Throwable?
)
