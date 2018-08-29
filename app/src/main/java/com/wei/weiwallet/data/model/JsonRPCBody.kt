package com.wei.weiwallet.data.model

/**
 * Created by enkaism on 2018/05/09.
 */
data class JsonRPCBody(
    val jsonrpc: String = "2.0",
    val id: Int = 1,
    val method: String,
    val params: Array<String>
)
