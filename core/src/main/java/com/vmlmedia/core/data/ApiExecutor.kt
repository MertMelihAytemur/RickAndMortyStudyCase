package com.vmlmedia.core.data

import com.vmlmedia.core.data.remote.ApiError
import com.vmlmedia.core.data.remote.ApiResult
import com.vmlmedia.core.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * This class is responsible for executing API calls and handling the response.
 * It returns an [ApiResult] which can be either [ApiResult.Success] or [ApiResult.Error].
 */
interface ApiExecutor {
    suspend fun <T> execute(
        call: suspend () -> Response<T>
    ): ApiResult<T> = withContext(Dispatchers.IO) {
        val response: Response<T>
        try {
            response = call.invoke()
            return@withContext if (response.isSuccessful) {
                ApiResult.Success(response.body())
            } else {
                when (response.code()) {
                    Constants.ERROR_CODE_SERVER -> ApiResult.Error(
                        ApiError.Authentication(response.errorBody())
                    )

                    else -> ApiResult.Error(ApiError.Server(response.errorBody()))
                }
            }
        } catch (exception: Exception) {
            return@withContext when (exception) {
                is ConnectException, is UnknownHostException -> ApiResult.Error(
                    ApiError.NoInternet(exception.message)
                )

                else -> ApiResult.Error(ApiError.IO(exception.message))
            }
        }
    }
}