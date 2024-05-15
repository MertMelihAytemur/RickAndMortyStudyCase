package com.vmlmedia.rickandmortystudycase.feature.splash.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vmlmedia.rickandmortystudycase.R
import com.vmlmedia.rickandmortystudycase.common.ext.navigateWithAnimation
import com.vmlmedia.rickandmortystudycase.core.ui.BaseFragment
import com.vmlmedia.rickandmortystudycase.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<SplashViewModel,FragmentSplashBinding>(
    FragmentSplashBinding::inflate
) {

    override val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSplash()
    }
    private fun initSplash() {
        lifecycleScope.launch {
            delay(DELAY_SPLASH)
            navigateWithAnimation(R.id.action_splashFragment_to_onBoardingFragment)
        }
    }

    companion object {
        private const val DELAY_SPLASH = 4000L
    }

}
