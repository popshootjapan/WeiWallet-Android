package com.wei.weiwallet.restoration

import androidx.lifecycle.ViewModel
import com.wei.weiwallet.di.ActivityScoped
import dagger.Binds
import dagger.Module

@Module
internal abstract class RestorationModule {
    @ActivityScoped
    @Binds
    abstract fun viewModel(viewModel: RestorationViewModel): ViewModel
}
