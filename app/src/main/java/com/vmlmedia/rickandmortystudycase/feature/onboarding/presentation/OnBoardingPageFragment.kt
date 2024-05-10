package com.vmlmedia.rickandmortystudycase.feature.onboarding.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vmlmedia.rickandmortystudycase.common.ext.getDrawableCompat
import com.vmlmedia.rickandmortystudycase.common.ext.getSerializable
import com.vmlmedia.rickandmortystudycase.databinding.FragmentOnBoardingPageBinding
import com.vmlmedia.rickandmortystudycase.feature.onboarding.domain.getText
import com.vmlmedia.rickandmortystudycase.feature.onboarding.domain.uimodel.OnBoardingPageUiModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OnBoardingPageFragment private constructor() : Fragment() {

    private var uiModel: OnBoardingPageUiModel? = null

    private lateinit var binding: FragmentOnBoardingPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiModel = arguments?.getSerializable(key = ARGS_UI_MODEL)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentOnBoardingPageBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyUiModel()
    }

    private fun applyUiModel() {
        uiModel?.apply {
            //binding.imageViewLogo.setImageDrawable(requireContext().getDrawableCompat(drawableRes))
            binding.textViewTitle.text = title.getText(requireContext())
            binding.textViewDescription.text = description.getText(requireContext())
        }
    }

    companion object{

        private const val ARGS_UI_MODEL = "ARGS_UI_MODEL"

        fun newInstance(
            uiModel: OnBoardingPageUiModel
        ): OnBoardingPageFragment {
            val args = Bundle()
            args.apply {
                putSerializable(ARGS_UI_MODEL, uiModel)
            }
            val fragment = OnBoardingPageFragment()
            fragment.arguments = args
            return fragment
        }
    }
}