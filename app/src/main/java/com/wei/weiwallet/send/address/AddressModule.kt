package com.wei.weiwallet.send.address

import androidx.lifecycle.ViewModel
import com.wei.weiwallet.di.ActivityScoped
import com.wei.weiwallet.di.FragmentScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by enkaism on 2018/05/22.
 */
@Module
internal abstract class AddressModule {
  @ActivityScoped
  @Binds
  abstract fun viewModel(viewModel: AddressViewModel): ViewModel

  @FragmentScoped
  @ContributesAndroidInjector
  internal abstract fun qrFragment(): QRFragment

  @FragmentScoped
  @ContributesAndroidInjector
  internal abstract fun addressFragment(): AddressFragment
}
