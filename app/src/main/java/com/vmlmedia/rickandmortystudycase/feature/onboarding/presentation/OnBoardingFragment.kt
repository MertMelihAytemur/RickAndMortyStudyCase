package com.vmlmedia.rickandmortystudycase.feature.onboarding.presentation

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.core.view.children
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.vmlmedia.rickandmortystudycase.R
import com.vmlmedia.rickandmortystudycase.common.ext.getDrawableCompat
import com.vmlmedia.rickandmortystudycase.common.ext.navigateWithAnimation
import com.vmlmedia.rickandmortystudycase.common.ext.setMargin
import com.vmlmedia.rickandmortystudycase.core.ui.BaseFragment
import com.vmlmedia.rickandmortystudycase.databinding.FragmentOnBoardingBinding
import com.vmlmedia.rickandmortystudycase.feature.onboarding.presentation.adapter.OnBoardingPagerAdapter
import com.vmlmedia.rickandmortystudycase.feature.onboarding.presentation.factory.OnBoardingPageFragmentFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : BaseFragment<OnBoardingViewModel, FragmentOnBoardingBinding>(
    FragmentOnBoardingBinding::inflate
) {
    override val viewModel: OnBoardingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initAdapter()
        initOnBackPressedHandler()
        initIndicator()
    }

    private fun initIndicator(){
        val size = OnBoardingPageFragmentFactory.size
        repeat(size){ position ->
            val radioButton = RadioButton(context)
            radioButton.id = generateOrGetId(position)
            radioButton.setMargin(resources.getDimensionPixelSize(R.dimen.fontSize_small))
            radioButton.isClickable = false
            radioButton.isFocusable = false
            radioButton.buttonDrawable = requireContext().getDrawableCompat(R.drawable.selector_indicator)
            binding.radioGroupIndicator.addView(radioButton)
        }
    }

    private fun initOnBackPressedHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
    }

    private fun onBackPressedBehaviour(){
        if (binding.viewPagerInformations.currentItem != 0) {
            binding.viewPagerInformations.currentItem -= 1
        } else{
            findNavController().navigateUp()
        }
    }

    private fun initAdapter(){
        binding.viewPagerInformations.apply {
            adapter = OnBoardingPagerAdapter(this@OnBoardingFragment)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.buttonNextOrGetStarted.text = if (position != LAST_INDEX){
                        getString(R.string.onBoarding_next)
                    } else
                        getString(R.string.onBoarding_getStarted)

                    binding.toolbar.isInvisible = position == PAGE_FIRST
                    binding.radioGroupIndicator.children.forEachIndexed { index, view ->
                        (view as RadioButton).isChecked = index == position
                    }
                }
            })
        }
    }

    private fun initListeners(){
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedBehaviour()
        }
        binding.buttonNextOrGetStarted.setOnClickListener {
            if (binding.viewPagerInformations.currentItem != OnBoardingPageFragmentFactory.size - 1)
                binding.viewPagerInformations.currentItem = binding.viewPagerInformations.currentItem + 1
            else {
                viewModel.setOnBoardingCompleted()
                navigateWithAnimation(R.id.action_onBoardingFragment_to_homeFragment)
            }
        }
    }

    private fun generateOrGetId(index: Int): Int = 1000 + index


    companion object {
        private const val PAGE_FIRST = 0
        private val LAST_INDEX = OnBoardingPageFragmentFactory.size - 1
    }
}