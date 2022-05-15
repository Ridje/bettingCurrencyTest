package com.kis.bettingcurrency.data.network

import com.kis.bettingcurrency.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

private class APIInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request
            .newBuilder()
            .addHeader("apiKey", "123")
            .build()
        return chain.proceed(newRequest)
    }
}

fun OkHttpClient.Builder.addProjectInterceptors(): OkHttpClient.Builder {
    if (BuildConfig.DEBUG) {
        addInterceptor(APIInterceptor())
        val loggingInterceptor = HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
        addInterceptor(loggingInterceptor)
    }
    return this
}