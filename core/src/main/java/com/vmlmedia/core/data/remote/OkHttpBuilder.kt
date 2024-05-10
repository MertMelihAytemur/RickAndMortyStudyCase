package com.vmlmedia.core.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class OkHttpBuilder {
    private val okHttpBuilder = OkHttpClient.Builder()

    fun addInterceptor(interceptor : Interceptor) : OkHttpBuilder {
        okHttpBuilder.addInterceptor(interceptor)
        return this
    }

    fun addAuthenticator(authenticator : okhttp3.Authenticator) : OkHttpBuilder {
        okHttpBuilder.authenticator(authenticator)
        return this
    }

    fun readTimeOut(timeout : Long, unit : TimeUnit) : OkHttpBuilder {
        okHttpBuilder.readTimeout(timeout, unit)
        return this
    }

    fun connectTimeOut(timeout : Long, unit : TimeUnit) : OkHttpBuilder {
        okHttpBuilder.connectTimeout(timeout, unit)
        return this
    }

    fun writeTimeOut(timeout : Long, unit : TimeUnit) : OkHttpBuilder {
        okHttpBuilder.writeTimeout(timeout, unit)
        return this
    }

    fun build() : OkHttpClient {
        return okHttpBuilder.build()
    }

}