package com.test.tiketchallenge.network

import com.test.tiketchallenge.stored.PreferencesManager
import okhttp3.Interceptor
import okhttp3.Response

class HttpHeaderInterceptor(private val preferencesManager: PreferencesManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response? {
        val request = chain?.request()

        val builder = request?.newBuilder()
        return chain?.proceed(builder?.build())
    }
}