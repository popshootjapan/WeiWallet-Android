package com.wei.weiwallet.launch

import androidx.lifecycle.ViewModel
import com.wei.wei.crypto.signEthereumMessage
import com.wei.wei.mnemonic.Mnemonic
import com.wei.wei.wallet.Wallet
import com.wei.weiwallet.data.SecretRepository
import com.wei.weiwallet.data.WeiRepository
import com.wei.weiwallet.data.model.JWTToken
import com.wei.weiwallet.data.model.SignBody
import io.reactivex.Observable
import javax.inject.Inject

class LaunchViewModel @Inject constructor(
        private val weiRepository: WeiRepository,
        private val secretRepository: SecretRepository
) : ViewModel() {
    fun sign(): Observable<JWTToken> {
        val mnemonic = Mnemonic.create()
        val seed = Mnemonic.createSeed(mnemonic)
        val wallet = Wallet(seed)
        val address = "0x" + wallet.getAddress()
        secretRepository.putPrivateKey(wallet.key.keyPair.privateKey)
        secretRepository.putAddress(wallet.getAddress())
        secretRepository.putMnemonic(mnemonic)
        val message = "Welcome to Wei wallet!"
        val sign = signEthereumMessage(message, wallet.key.keyPair)
        return weiRepository.sign(SignBody(address, sign))
    }

    fun isSigned(): Boolean = secretRepository.token().isNotEmpty()
    fun putToken(token: String) {
        secretRepository.putToken(token)
    }

    fun addLaunchCount() {
        val launchCount = secretRepository.launchCount() + 1
        secretRepository.addLaunchCount(launchCount)
    }

    fun address() = secretRepository.address()
}
