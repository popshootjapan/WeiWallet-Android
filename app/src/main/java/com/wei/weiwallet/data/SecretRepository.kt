package com.wei.weiwallet.data

import android.content.SharedPreferences
import com.wei.wei.crypto.publicKeyFromPrivate
import java.math.BigInteger
import javax.inject.Inject

class SecretRepository @Inject constructor(
        private val sharedPreferences: SharedPreferences) : SecretDataSource {
    override fun address(): String {
        val address = sharedPreferences.getString("address", "")
        return if (address.startsWith("0x")) {
            address
        } else {
            "0x$address"
        }
    }

    override fun putAddress(address: String) {
        sharedPreferences.edit().putString("address", address).apply()
    }

    override fun privateKey(): BigInteger = BigInteger(
            sharedPreferences.getString("private_key", "0"))

    override fun putPrivateKey(privateKey: BigInteger) {
        sharedPreferences.edit().putString("private_key", privateKey.toString()).apply()
    }

    override fun publicKey(): BigInteger = publicKeyFromPrivate(BigInteger(
            sharedPreferences.getString("private_key", "0")))

    override fun token(): String = sharedPreferences.getString("token", "")

    override fun putToken(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
    }

    override fun nonce(): Int = sharedPreferences.getInt("nonce", 0)

    override fun putNonce(nonce: Int) {
        sharedPreferences.edit().putInt("nonce", nonce).apply()
    }

    override fun launchCount(): Int = sharedPreferences.getInt("launch_count", 0)

    override fun addLaunchCount(launchCount: Int) {
        sharedPreferences.edit().putInt("launch_count", launchCount).apply()
    }

    override fun isShownOnBoarding(): Boolean = sharedPreferences.getBoolean("on_boarding", true)

    override fun putIsShownOnBoarding() {
        sharedPreferences.edit().putBoolean("on_boarding", false).apply()
    }

    override fun putMnemonic(mnemonic: String) {
        sharedPreferences.edit().putString("mnemonic", mnemonic).apply()
    }

    override fun mnemonic(): String = sharedPreferences.getString("mnemonic", "")

    override fun isAlreadyBackup(): Boolean = sharedPreferences.getBoolean("isAlreadyBackup", false)

    override fun putIsAlreadyBackup() {
        sharedPreferences.edit().putBoolean("isAlreadyBackup", true).apply()
    }
}
