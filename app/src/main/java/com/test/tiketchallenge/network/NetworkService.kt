package com.test.tiketchallenge.network

import com.test.tiketchallenge.BuildConfig
import com.test.tiketchallenge.network.response.AccountGithubResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService{

    @GET(BuildConfig.ENDPOINT + "search/users")
    fun fetchGithubAccount(@Query("q") accountName : String?) : Observable<AccountGithubResponse>

    @GET(BuildConfig.ENDPOINT + "search/users?q=deasmahaputra")
    fun getDeas() : Observable<AccountGithubResponse>
}