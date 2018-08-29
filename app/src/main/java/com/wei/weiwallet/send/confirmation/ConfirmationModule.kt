package com.wei.weiwallet.send.confirmation

import androidx.lifecycle.ViewModel
import com.wei.weiwallet.di.ActivityScoped
import dagger.Binds
import dagger.Module

@Module
internal abstract class ConfirmationModule {
  @ActivityScoped
  @Binds
  abstract fun viewModel(viewModel: ConfirmationViewModel): ViewModel
}
