package com.vmlmedia.rickandmortystudycase.feature.onboarding.domain.uimodel

import androidx.annotation.DrawableRes
import com.vmlmedia.rickandmortystudycase.feature.onboarding.domain.UiString
import java.io.Serializable

data class OnBoardingPageUiModel(
    @DrawableRes val drawableRes: Int,
    val title: UiString,
    val description: UiString
) : Serializable