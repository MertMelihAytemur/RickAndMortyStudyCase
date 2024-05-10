package com.vmlmedia.rickandmortystudycase.feature.onboarding.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vmlmedia.rickandmortystudycase.feature.onboarding.presentation.factory.OnBoardingPageFragmentFactory

class OnBoardingPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = OnBoardingPageFragmentFactory.size

    override fun createFragment(position: Int): Fragment {
        return OnBoardingPageFragmentFactory.create(position)
    }
}