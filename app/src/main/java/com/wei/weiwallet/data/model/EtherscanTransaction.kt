package com.wei.weiwallet.data.model

import com.wei.weiwallet.ext.convertEther
import java.text.SimpleDateFormat
import java.util.*

data class EtherscanTransaction(
        val timeStamp: String,
        val value: String,
        val from: String,
        val to: String,
        val confirmations: String
) {
    fun displayDate(): String {
        val unixSeconds: Long = timeStamp.toLong()
        val date = Date(unixSeconds * 1000L)
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm")
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    fun displayValue(): String = String.format("%.6f ETH", value.toDouble().convertEther())

    fun address(myAddress: String): String = if (isReceiveTransaction(myAddress)) from else to
    fun displaySenderAndReceiver(myAddress: String): String =
            if (isReceiveTransaction(myAddress)) "from" else "to"

    // NOTE: Addresses returned from Etherscan is lowercase.
    // To check correctly, you need to lowercase the address.
    fun isReceiveTransaction(myAddress: String): Boolean = to == myAddress.toLowerCase()

    // TODO to string.xml
    fun isReceive(myAddress: String): String = if (isReceiveTransaction(myAddress)) "受け取り済み" else "送金済み"

    fun isExecutedLessThanDay(): Boolean {
        val transactionUnixtime = timeStamp.toLong()
        val transactionDate = Date(transactionUnixtime * 1000)

        val currentUnixtime = System.currentTimeMillis() / 1000
        val currentDate = Date(currentUnixtime * 1000)

        val oneDateTime = (1000 * 60 * 60 * 24).toLong()
        val diffDays = (currentDate.time - transactionDate.time) / oneDateTime
        return diffDays.toInt() == 0
    }
}
