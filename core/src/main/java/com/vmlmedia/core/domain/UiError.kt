package com.vmlmedia.core.domain

import com.google.gson.Gson
import com.vmlmedia.core.data.remote.ApiError
import com.vmlmedia.core.data.remote.ApiResult

sealed class UiError<out T> {

    data class NoInternet(val message: String? = null) : UiError<Nothing>()

    data class Authentication<T>(val errorBody: T? = null) : UiError<T>()

    data class Server<T>(val errorBody: T? = null) : UiError<T>()

    data class IO(val message: String? = null) : UiError<Nothing>()

}

inline fun <reified E> parseError(apiResult: ApiResult.Error): UiResult.Error<E> {
    return when (apiResult.error) {
        is ApiError.Server -> {
            try {
                val error = Gson().fromJson(
                    apiResult.error.errorBody?.string(),
                    E::class.java
                )
                UiResult.Error(UiError.Server(error))
            } catch (e: Exception) {
                UiResult.Error(UiError.Server())
            }
        }

        is ApiError.Authentication -> {
            try {
                val error = Gson().fromJson(
                    apiResult.error.errorBody?.string(),
                    E::class.java
                )
                UiResult.Error(UiError.Authentication(error))
            } catch (e: Exception) {
                UiResult.Error(UiError.Authentication())
            }
        }

        is ApiError.IO -> UiResult.Error(UiError.IO(message = apiResult.error.message))
        is ApiError.NoInternet -> UiResult.Error(UiError.NoInternet(message = apiResult.error.message))
    }
}