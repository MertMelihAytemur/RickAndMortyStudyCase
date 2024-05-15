package com.vmlmedia.core.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.vmlmedia.core.data.Visibility
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class CoreFragment<VM: CoreViewModel> : Fragment() {
    abstract val viewModel: VM
    abstract val loadingInterface: LoadingInterface

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadingStateFlow.collect {
                if (it.show == Visibility.SHOW) {
                    loadingInterface.show()
                } else if (it.show == Visibility.HIDE) {
                    loadingInterface.hide()
                }
            }
        }
    }

    /**
     * Collects the page state and executes the block when the state changes
     * @param pageState the state to collect
     */
    fun <T> collectPageState(pageState: StateFlow<T>, block: (T) -> Unit){
        viewLifecycleOwner.lifecycleScope.launch {
            pageState.collect { state ->
                block(state)
            }
        }
    }

}