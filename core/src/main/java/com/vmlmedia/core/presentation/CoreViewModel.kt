package com.vmlmedia.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vmlmedia.core.data.LoadingState
import com.vmlmedia.core.data.Visibility
import com.vmlmedia.core.domain.UiError
import com.vmlmedia.core.domain.UiModel
import com.vmlmedia.core.domain.UiResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * CoreViewModel is a base class for all view models in the application.
 * It provides a loading state flow to observe loading state changes.
 * It also provides a method to launch requests with loading state changes.
 * @see LoadingState
 * @see Visibility
 * @see UiModel
 * @see UiResult
 * @see UiError
 */
abstract class CoreViewModel : ViewModel() {
    private val loadingStateChannel: Channel<LoadingState> = Channel()

    private var loadingCount: Int = 0

    /**
     * StateFlow kullanılmaya devam ediliyor. Race condition'dan kaçınmak için
     * channel kullanıldı. Sonrasında channel içindeki veriler state flow'a aktarıldı
     */

    val loadingStateFlow = loadingStateChannel.consumeAsFlow()
        .map { visibilityChangeEvent ->
            updateLoadingCountBasedOnVisibility(visibilityChangeEvent.show)
            createLoadingStateBasedOnCount()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = LoadingState(Visibility.IDLE)
        )


    fun <T : UiModel, E> launchRequest(
        requestBody: suspend () -> UiResult<T?, E>,
        onSuccess: (suspend (T?) -> Unit)? = null,
        onError: (suspend (UiError<E>) -> Unit)? = null,
        showLoading: Boolean = true
    ): Job {
        return viewModelScope.launch {
            if (showLoading) {
                changeLoadingState(LoadingState(Visibility.SHOW))
            }

            when (val result = requestBody.invoke()) {
                is UiResult.Success -> {
                    changeLoadingState(LoadingState(Visibility.HIDE))
                    onSuccess?.invoke(result.response)
                }

                is UiResult.Error -> {
                    changeLoadingState(LoadingState(Visibility.HIDE))
                    onError?.invoke(result.error)
                }
            }
        }
    }

    private fun updateLoadingCountBasedOnVisibility(visibility: Visibility) {
        when (visibility) {
            Visibility.SHOW -> loadingCount++
            Visibility.HIDE -> loadingCount--
            Visibility.IDLE -> {}
        }
    }

    private fun createLoadingStateBasedOnCount(): LoadingState {
        return if (loadingCount == 0) {
            LoadingState(Visibility.HIDE)
        } else {
            LoadingState(Visibility.SHOW)
        }
    }

    /**
     * Changes the loading state of the view.
     * @param loadingState The new loading state.
     */
    suspend fun changeLoadingState(loadingState: LoadingState) {
        loadingStateChannel.send(loadingState)
    }
}