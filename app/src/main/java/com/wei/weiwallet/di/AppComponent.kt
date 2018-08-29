package com.wei.weiwallet.di

import com.wei.weiwallet.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by enkaism on 2018/04/27.
 */
@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class, DataModule::class,
    ActivityBindingModule::class))
interface AppComponent : AndroidInjector<App> {

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: App): Builder

    fun build(): AppComponent
  }

  override fun inject(app: App)
}
