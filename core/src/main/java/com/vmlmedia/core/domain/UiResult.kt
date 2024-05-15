package com.vmlmedia.core.domain

import com.vmlmedia.core.data.remote.ApiError

sealed class UiResult<out T, out E> {
    class Success<T : UiModel>(val response: T?) : UiResult<T,Nothing>()

    class Error<E>(val error : UiError<E>) : UiResult<Nothing,E>()
}