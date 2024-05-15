package com.vmlmedia.rickandmortystudycase.feature.onboarding.presentation.factory

import com.vmlmedia.rickandmortystudycase.R
import com.vmlmedia.rickandmortystudycase.feature.onboarding.domain.UiString
import com.vmlmedia.rickandmortystudycase.feature.onboarding.domain.uimodel.OnBoardingPageUiModel
import com.vmlmedia.rickandmortystudycase.feature.onboarding.presentation.OnBoardingPageFragment

class OnBoardingPageFragmentFactory private constructor(
    private val page: Int
): Factory<OnBoardingPageFragment> {

    override fun create(): OnBoardingPageFragment =
        OnBoardingPageFragment.newInstance(uiModels[page])

    companion object{

        private val uiModels = arrayOf(
            OnBoardingPageUiModel(
                drawableRes = androidx.core.R.drawable.notification_icon_background,
                title = UiString.PlainResource(R.string.onBoardingPage_first_title),
                description = UiString.PlainResource(R.string.onBoardingPage_first_description)
            ),
            OnBoardingPageUiModel(
                drawableRes = androidx.core.R.drawable.notification_icon_background,
                title = UiString.PlainResource(R.string.onBoardingPage_second_title),
                description = UiString.PlainResource(R.string.onBoardingPage_second_description)
            ),
            OnBoardingPageUiModel(
                drawableRes = androidx.core.R.drawable.notification_icon_background,
                title = UiString.PlainResource(R.string.onBoardingPage_third_title),
                description = UiString.PlainResource(R.string.onBoardingPage_third_description)
            ),
        )

        val size = uiModels.size

        fun create(page: Int): OnBoardingPageFragment {
            val factory = OnBoardingPageFragmentFactory(page)
            return factory.create()
        }
    }
}