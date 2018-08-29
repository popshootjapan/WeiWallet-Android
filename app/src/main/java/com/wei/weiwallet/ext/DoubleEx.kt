package com.wei.weiwallet.ext

fun Double.convertEther(): Double {
    // NOTE: calculate wei by 10^18
    return div(10e17)
}

fun Double.toWei(): Double = this.times(10e17)
