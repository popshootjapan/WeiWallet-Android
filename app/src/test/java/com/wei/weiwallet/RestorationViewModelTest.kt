package com.wei.weiwallet

import com.wei.weiwallet.data.SecretRepository
import com.wei.weiwallet.data.WeiRepository
import com.wei.weiwallet.restoration.RestorationViewModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

class RestorationViewModelTest {
    @Test
    fun isValidMnemonic_isCorrect() {
        val weiRepositoryMock = mock(WeiRepository::class.java)
        val secretRepositoryMock = mock(SecretRepository::class.java)
        val viewModel = RestorationViewModel(weiRepositoryMock, secretRepositoryMock)
        val testData = arrayOf(
                Pair(true, "fetch reopen parent swap plug cave maid mean race dentist prison arrow"),
                Pair(false, "app reopen parent swap plug cave maid mean race dentist prison arrow"),
                Pair(false, "")
        )
        testData.forEach {
            assertEquals(viewModel.isValidMnemonic(it.second), it.first)
        }
    }
}
