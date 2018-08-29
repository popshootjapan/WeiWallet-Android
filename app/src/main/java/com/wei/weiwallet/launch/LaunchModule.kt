package com.wei.weiwallet.launch

import androidx.lifecycle.ViewModel
import com.wei.weiwallet.di.ActivityScoped
import dagger.Binds
import dagger.Module

@Module
internal abstract class LaunchModule {
  @ActivityScoped
  @Binds
  abstract fun viewModel(viewModel: LaunchViewModel): ViewModel
}
