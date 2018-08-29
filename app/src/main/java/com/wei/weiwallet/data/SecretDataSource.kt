package com.wei.weiwallet.data

import java.math.BigInteger

interface SecretDataSource {
  fun address(): String
  fun putAddress(address: String)
  fun privateKey(): BigInteger
  fun putPrivateKey(privateKey: BigInteger)
  fun publicKey(): BigInteger
  fun token(): String
  fun putToken(token: String)
  fun nonce(): Int
  fun putNonce(nonce: Int)
  fun launchCount(): Int
  fun addLaunchCount(launchCount: Int)
  fun isShownOnBoarding(): Boolean
  fun putIsShownOnBoarding()
  fun putMnemonic(mnemonic: String)
  fun mnemonic(): String
  fun isAlreadyBackup(): Boolean
  fun putIsAlreadyBackup()
}
