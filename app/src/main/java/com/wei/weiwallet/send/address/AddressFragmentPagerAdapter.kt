package com.wei.weiwallet.send.address

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.wei.weiwallet.R

class AddressFragmentPagerAdapter(fragmentManager: FragmentManager,
    context: Context) : FragmentPagerAdapter(
    fragmentManager) {

  private val titles: Array<String> = context.resources.getStringArray(R.array.send_address_tab)

  override fun getItem(position: Int): Fragment =
      when (position) {
        0 -> QRFragment()
        1 -> AddressFragment()
        else -> AddressFragment()
      }

  override fun getCount(): Int = 2

  override fun getPageTitle(position: Int): CharSequence {
    return titles[position]
  }

}
