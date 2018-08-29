package com.wei.weiwallet.transaction_list

import androidx.lifecycle.ViewModel
import com.wei.weiwallet.di.ActivityScoped
import dagger.Binds
import dagger.Module

@Module
internal abstract class TransactionListModule {
  @ActivityScoped
  @Binds
  abstract fun viewModel(viewModel: TransactionListViewModel): ViewModel
}
