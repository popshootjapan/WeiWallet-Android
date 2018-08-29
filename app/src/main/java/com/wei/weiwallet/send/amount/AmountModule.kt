package com.wei.weiwallet.send.amount

import androidx.lifecycle.ViewModel
import com.wei.weiwallet.di.ActivityScoped
import dagger.Binds
import dagger.Module

/**
 * Created by enkaism on 2018/05/22.
 */
@Module
internal abstract class AmountModule {
  @ActivityScoped
  @Binds
  abstract fun viewModel(viewModel: AmountViewModel): ViewModel
}
