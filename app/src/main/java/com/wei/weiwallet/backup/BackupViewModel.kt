package com.wei.weiwallet.backup

import androidx.lifecycle.ViewModel
import com.wei.weiwallet.data.SecretRepository
import javax.inject.Inject


class BackupViewModel @Inject constructor(
        private val secretRepository: SecretRepository
) : ViewModel() {
    fun mnemonic() = secretRepository.mnemonic()
    fun putIsAlreadyBackup() {
        secretRepository.putIsAlreadyBackup()
    }
}
