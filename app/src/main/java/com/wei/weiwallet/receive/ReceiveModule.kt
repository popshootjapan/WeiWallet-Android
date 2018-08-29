package com.wei.weiwallet.receive

import androidx.lifecycle.ViewModel
import com.wei.weiwallet.di.ActivityScoped
import dagger.Binds
import dagger.Module

/**
 * Created by enkaism on 2018/05/22.
 */
@Module
internal abstract class ReceiveModule {
  @ActivityScoped
  @Binds
  abstract fun viewModel(viewModel: ReceiveViewModel): ViewModel
}
