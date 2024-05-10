package com.vmlmedia.rickandmortystudycase.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.vmlmedia.core.domain.UiError
import com.vmlmedia.core.presentation.CoreFragment
import com.vmlmedia.core.presentation.CoreViewModel
import com.vmlmedia.core.presentation.LoadingInterface
import com.vmlmedia.rickandmortystudycase.core.model.ApiErrorModel

typealias Inflater<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VM : CoreViewModel, VB : ViewBinding>(
    private val inflater: Inflater<VB>,
) : CoreFragment<VM>(){

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override val loadingInterface: LoadingInterface
        get() = (requireActivity() as MainActivity).loadingDialog
    //private val errorDialogFragment by lazy { ErrorBottomSheetDialogFragment(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = this.inflater.invoke(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun handleNetworkError(
        error: UiError<ApiErrorModel>,
        buttonText: String? = null,
        dialogMessageText: String? = null
    ) {

    }

}