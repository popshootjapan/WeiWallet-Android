package com.wei.weiwallet.main

import androidx.lifecycle.ViewModel
import com.wei.weiwallet.backup.BackupFragment
import com.wei.weiwallet.backup.BackupViewModel
import com.wei.weiwallet.di.ActivityScoped
import com.wei.weiwallet.di.FragmentScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by enkaism on 2018/04/28.
 */
@Module
internal abstract class MainModule {
  @ActivityScoped
  @Binds
  abstract fun viewModel(viewModel: MainViewModel): ViewModel

  @FragmentScoped
  @ContributesAndroidInjector
  internal abstract fun mainFragment(): MainFragment

  @FragmentScoped
  @ContributesAndroidInjector
  internal abstract fun backupFragment(): BackupFragment
}

