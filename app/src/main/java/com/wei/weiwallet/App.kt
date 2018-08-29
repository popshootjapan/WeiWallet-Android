package com.wei.weiwallet

import com.wei.weiwallet.di.DaggerAppComponent
import dagger.android.support.DaggerApplication

/**
 * Created by enkaism on 2018/04/27.
 */
class App : DaggerApplication() {
  override fun applicationInjector() = DaggerAppComponent.builder()
      .application(this)
      .build()
}
