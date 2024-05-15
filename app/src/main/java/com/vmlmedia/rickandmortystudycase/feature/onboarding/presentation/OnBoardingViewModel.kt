package com.vmlmedia.rickandmortystudycase.feature.onboarding.presentation

import com.vmlmedia.core.presentation.CoreViewModel
import com.vmlmedia.rickandmortystudycase.common.ClientPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val clientPreferences: ClientPreferences
) : CoreViewModel() {

    fun setOnBoardingCompleted() {
        clientPreferences.setHasOnBoarding(true)
    }
}