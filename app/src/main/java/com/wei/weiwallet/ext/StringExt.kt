package com.wei.weiwallet.ext

fun String.toStringWithSeparator() = this.reversed().withIndex().map {it.value + if ((it.index-1) % 3 == 2) "," else ""}.reversed().joinToString(separator = "")
