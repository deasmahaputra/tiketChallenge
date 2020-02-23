package com.test.tiketchallenge.network

interface ApiResponseCallback<T> {
    fun onSuccess(response: T)
    fun onError(error: NetworkError)
}