package com.vmlmedia.core.data.remote

import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit

class RetrofitBuilder {
    private val retrofitBuilder = Retrofit.Builder()

    fun baseUrl(baseUrl: String): RetrofitBuilder {
        retrofitBuilder.baseUrl(baseUrl)
        return this
    }

    fun addConverterFactory(factory: Converter.Factory): RetrofitBuilder {
        retrofitBuilder.addConverterFactory(factory)
        return this
    }

    fun addCallAdapterFactory(factory: CallAdapter.Factory): RetrofitBuilder {
        retrofitBuilder.addCallAdapterFactory(factory)
        return this
    }

    fun client(client: OkHttpClient): RetrofitBuilder {
        retrofitBuilder.client(client)
        return this
    }

    fun build(): Retrofit {
        return retrofitBuilder.build()
    }
}