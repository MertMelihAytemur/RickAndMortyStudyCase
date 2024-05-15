package com.vmlmedia.rickandmortystudycase.feature.splash.presentation

import com.vmlmedia.core.presentation.CoreViewModel
import com.vmlmedia.rickandmortystudycase.common.ClientPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val clientPreferences: ClientPreferences
): CoreViewModel() {

    fun isOnBoardingDone() = clientPreferences.isOnBoardingDone()
}