package com.wei.weiwallet.onboarding

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import com.wei.weiwallet.R
import com.wei.weiwallet.databinding.DialogOnboardingBinding
import com.wei.weiwallet.databinding.ItemOnboardingViewPagerBinding
import kotlinx.android.synthetic.main.dialog_onboarding.*


class OnBoardingDialog : DialogFragment(), ViewPager.OnPageChangeListener {
    var finishCallback: (() -> Unit)? = null
    var finishText: String? = null

    private lateinit var binding: DialogOnboardingBinding
    private var pagerAdapter: PagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        return DataBindingUtil.inflate<DialogOnboardingBinding>(inflater, R.layout.dialog_onboarding, container,
                false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerAdapter = OnBoardingAdapter(this.context!!,
                intArrayOf(R.drawable.ic_onboarding_first, R.drawable.ic_onboarding_second, R.drawable.ic_onboarding_third)
                , resources.getStringArray(R.array.on_boarding_title).toList()
                , resources.getStringArray(R.array.on_boarding_message).toList()
        )

        view_pager_container?.adapter = pagerAdapter
        view_pager_container?.addOnPageChangeListener(this)
        view_pager_container?.currentItem = 0

        intro_btn_finish.setOnClickListener { finishCallback?.invoke() }
        finishText?.let { intro_btn_finish.text = it }

        intro_btn_next.setOnClickListener { view_pager_container?.currentItem?.let { view_pager_container?.currentItem = it + 1 } }
        onPageSelected(0)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewPagerIndicator.setupWithViewPager(binding.viewPagerContainer)
    }

    override fun onPageScrollStateChanged(p0: Int) {
        Log.d("debug", "onPageScrollStateChanged")
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
        Log.d("debug", "onPageScrolled")
    }

    override fun onPageSelected(position: Int) {
        Log.d("debug: onPageSelected", position.toString())
        intro_btn_next.visibility = if (position + 1 == pagerAdapter?.count) View.GONE else View.VISIBLE
        intro_btn_finish.visibility = if (position + 1 == pagerAdapter?.count) View.VISIBLE else View.GONE
    }

    private inner class OnBoardingAdapter(val context: Context, private val images: IntArray,
                                          private val titleArray: List<String>, private val descriptionArray: List<String>) : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int = images.size

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val binding = ItemOnboardingViewPagerBinding.inflate(LayoutInflater.from(context), container, false)
            binding.image.setImageResource(images[position])
            binding.title.text = titleArray[position]
            binding.description.text = descriptionArray[position]
            container.addView(binding.root)

            return binding.root
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as LinearLayout)
        }
    }
}
