package com.test.tiketchallenge.network

import com.test.tiketchallenge.network.response.AccountGithubResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService{

    @GET(BaseUrl.BASE_URL + "search/users")
    fun fetchGithubAccount(@Query("q") accountName : String?) : Observable<AccountGithubResponse>
}