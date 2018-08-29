package com.wei.weiwallet.restoration

import androidx.lifecycle.ViewModel
import com.wei.wei.crypto.signEthereumMessage
import com.wei.wei.mnemonic.Mnemonic
import com.wei.wei.mnemonic.WordList
import com.wei.wei.wallet.Wallet
import com.wei.weiwallet.data.SecretRepository
import com.wei.weiwallet.data.WeiRepository
import com.wei.weiwallet.data.model.JWTToken
import com.wei.weiwallet.data.model.SignBody
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class RestorationViewModel @Inject constructor(
        private val weiRepository: WeiRepository,
        private val secretRepository: SecretRepository
) : ViewModel() {
    val restoreSubject: PublishSubject<String> = PublishSubject.create<String>()

    fun restore(mnemonic: String): Observable<JWTToken> {
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

    fun isValidMnemonic(mnemonic: String): Boolean {
        return mnemonic.split(" ").fold(true, { acc, s -> acc && isValidWord(s) })
    }

    private fun isValidWord(word: String): Boolean {
        return WordList.ENGLISH.words.contains(word)
    }

    fun putToken(token: String) {
        secretRepository.putToken(token)
    }
}
