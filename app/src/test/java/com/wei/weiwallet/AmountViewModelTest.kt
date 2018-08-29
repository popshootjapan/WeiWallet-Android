package com.wei.weiwallet

import com.wei.wei.model.DEFAULT_GAS_LIMIT
import com.wei.wei.model.DEFAULT_GAS_PRICE
import com.wei.weiwallet.data.SecretRepository
import com.wei.weiwallet.data.TransactionsRepository
import com.wei.weiwallet.data.WeiRepository
import com.wei.weiwallet.ext.convertEther
import com.wei.weiwallet.send.amount.AmountViewModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import java.math.BigDecimal

class AmountViewModelTest {
    @Test
    fun calculateFee_isCorrect() {
        val transactionRepositoryMock = mock(TransactionsRepository::class.java)
        val weiRepositoryMock = mock(WeiRepository::class.java)
        val secretRepositoryMock = mock(SecretRepository::class.java)
        val viewModel = AmountViewModel(transactionRepositoryMock, weiRepositoryMock, secretRepositoryMock)
        val testData = arrayOf(FeeTestData("12000", 10), FeeTestData("180000", 154))
        testData.forEach {
            assertEquals(it.expect, viewModel.calculateFee(it.rate))
        }
    }

    @Test
    fun calculateFeeEth_isCorrect() {
        val transactionRepositoryMock = mock(TransactionsRepository::class.java)
        val weiRepositoryMock = mock(WeiRepository::class.java)
        val secretRepositoryMock = mock(SecretRepository::class.java)
        val viewModel = AmountViewModel(transactionRepositoryMock, weiRepositoryMock, secretRepositoryMock)
        assertEquals(viewModel.calculateFeeEth(), BigDecimal.valueOf((DEFAULT_GAS_LIMIT * DEFAULT_GAS_PRICE).toDouble().convertEther()).toPlainString())
    }

    @Test
    fun calculateAvailableAmount_isCorrect() {
        val transactionRepositoryMock = mock(TransactionsRepository::class.java)
        val weiRepositoryMock = mock(WeiRepository::class.java)
        val secretRepositoryMock = mock(SecretRepository::class.java)
        val viewModel = AmountViewModel(transactionRepositoryMock, weiRepositoryMock, secretRepositoryMock)
        val testData = arrayOf(AvailableAmountTestData("0xffffff", "12000", "0.000000"))
        testData.forEach {
            assertEquals(it.expect, viewModel.calculateAvailableAmount(it.balanceHex, it.rate))
        }
    }
}

data class FeeTestData(val rate: String, val expect: Int)
data class AvailableAmountTestData(val balanceHex: String, val rate: String, val expect: String)
