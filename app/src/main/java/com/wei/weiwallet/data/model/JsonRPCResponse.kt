package com.wei.weiwallet.data.model

/**
 * Created by enkaism on 2018/05/11.
 */
data class JsonRPCResponse<out T>(
    val jsonrpc: String,
    val id: Int,
    val result: T
)
