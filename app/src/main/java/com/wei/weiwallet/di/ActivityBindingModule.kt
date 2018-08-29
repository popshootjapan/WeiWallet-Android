package com.wei.weiwallet.di

import com.wei.weiwallet.launch.LaunchActivity
import com.wei.weiwallet.launch.LaunchModule
import com.wei.weiwallet.main.MainActivity
import com.wei.weiwallet.main.MainModule
import com.wei.weiwallet.receive.ReceiveActivity
import com.wei.weiwallet.receive.ReceiveModule
import com.wei.weiwallet.restoration.RestorationActivity
import com.wei.weiwallet.restoration.RestorationModule
import com.wei.weiwallet.send.address.AddressActivity
import com.wei.weiwallet.send.address.AddressModule
import com.wei.weiwallet.send.amount.AmountActivity
import com.wei.weiwallet.send.amount.AmountModule
import com.wei.weiwallet.send.confirmation.ConfirmationActivity
import com.wei.weiwallet.send.confirmation.ConfirmationModule
import com.wei.weiwallet.transaction_list.TransactionListActivity
import com.wei.weiwallet.transaction_list.TransactionListModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by enkaism on 2018/04/27.
 */
@Module
internal abstract class ActivityBindingModule {
  @ActivityScoped
  @ContributesAndroidInjector(modules = arrayOf(LaunchModule::class))
  internal abstract fun launchActivity(): LaunchActivity

  @ActivityScoped
  @ContributesAndroidInjector(modules = arrayOf(MainModule::class))
  internal abstract fun mainActivity(): MainActivity

  @ActivityScoped
  @ContributesAndroidInjector(modules = arrayOf(ReceiveModule::class))
  internal abstract fun receiveActivity(): ReceiveActivity

  @ActivityScoped
  @ContributesAndroidInjector(modules = arrayOf(AddressModule::class))
  internal abstract fun addressActivity(): AddressActivity

  @ActivityScoped
  @ContributesAndroidInjector(modules = arrayOf(AmountModule::class))
  internal abstract fun amountActivity(): AmountActivity

  @ActivityScoped
  @ContributesAndroidInjector(modules = arrayOf(ConfirmationModule::class))
  internal abstract fun confirmationActivity(): ConfirmationActivity

  @ContributesAndroidInjector(modules = arrayOf(TransactionListModule::class))
  internal abstract fun transactionListActivity(): TransactionListActivity

  @ContributesAndroidInjector(modules = arrayOf(RestorationModule::class))
  internal abstract fun restorationActivity(): RestorationActivity

}

