package com.test.tiketchallenge.network

import com.google.gson.Gson
import com.test.tiketchallenge.extension.takeIfNotEmpty
import com.test.tiketchallenge.network.response.ErrorResponse
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class NetworkError(private val throwable: Throwable) {

    companion object {
        const val MESSAGE_ERROR_DEFAULT = "Something went wrong."
        const val MESSAGE_ERROR_CONNECTION = "There's problem with your connection."
    }

    private val errorBody = if (throwable is HttpException) throwable.response().errorBody()?.string() else ""


    fun getErrorMessage(): String {
        if (throwable is IOException && throwable !is SocketTimeoutException) {
            return MESSAGE_ERROR_CONNECTION
        }
        if (throwable is HttpException) {
            getErrorResponse().let { it ->
                it?.error.let { return it?.takeIfNotEmpty(MESSAGE_ERROR_DEFAULT).toString() }
            }
        }
        return MESSAGE_ERROR_DEFAULT
    }

    fun isNetworkError(): Boolean = (throwable is IOException && throwable !is SocketTimeoutException)

    fun getErrorCode(): Int {
        return if (throwable is HttpException) {
            throwable.response().code()
        } else {
            0
        }
    }

    private fun getErrorResponse(): ErrorResponse? {
        return try {
            Gson().fromJson(errorBody, ErrorResponse::class.java)
        } catch (e: IOException) {
            null
        } catch (e: Exception) {
            null
        }
    }
}