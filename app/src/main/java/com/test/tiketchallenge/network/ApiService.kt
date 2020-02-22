package com.test.tiketchallenge.network

import com.test.tiketchallenge.network.response.AccountGithubResponse
import io.reactivex.ObservableSource

class ApiService(private val networkService: NetworkService){

    fun fetchGithubAccount(name : String) : ObservableSource<AccountGithubResponse>{
        return networkService.fetchGithubAccount(name)
    }

}