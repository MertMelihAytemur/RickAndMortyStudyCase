package com.vmlmedia.rickandmortystudycase.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.vmlmedia.core.domain.UiError
import com.vmlmedia.core.presentation.CoreFragment
import com.vmlmedia.core.presentation.CoreViewModel
import com.vmlmedia.core.presentation.LoadingInterface
import com.vmlmedia.rickandmortystudycase.core.model.ApiErrorModel

typealias Inflater<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VM : CoreViewModel, VB : ViewBinding>(
    private val inflater: Inflater<VB>,
) : CoreFragment<VM>() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override val loadingInterface: LoadingInterface
        get() = (requireActivity() as MainActivity).loadingDialog

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
    ) {
        when (error) {
            is UiError.Authentication -> Toast.makeText(
                requireContext(),
                "Authentication error",
                Toast.LENGTH_SHORT
            ).show()

            is UiError.NoInternet -> Toast.makeText(
                requireContext(),
                "No internet connection",
                Toast.LENGTH_SHORT
            ).show()

            is UiError.Server -> Toast.makeText(
                requireContext(),
                "Server error",
                Toast.LENGTH_SHORT
            ).show()

            is UiError.IO -> Toast.makeText(requireContext(), "IO error", Toast.LENGTH_SHORT).show()
        }
    }
}