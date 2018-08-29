package com.wei.weiwallet.data.model

/**
 * Created by enkaism on 2018/05/11.
 */
data class SignBody(
    val address: String,
    val sign: String,
    val token: String = ""
)
