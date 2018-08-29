package com.wei.weiwallet.data.model

data class EtherscanResponse(
  val status: String,
  val message: String,
  val result: List<EtherscanTransaction>
)
