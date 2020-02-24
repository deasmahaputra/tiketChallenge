package com.test.tiketchallenge.network

import com.test.tiketchallenge.extension.configured
import com.test.tiketchallenge.extension.subscribes
import com.test.tiketchallenge.network.response.AccountGithubResponse
import io.reactivex.ObservableSource
import io.reactivex.disposables.Disposable

class ApiService(private val networkService: NetworkService){

    fun fetchGithubAccount(name : String, page : Int) : ObservableSource<AccountGithubResponse>{
        return networkService.fetchGithubAccount(name, page)
    }

    fun getData(callback: ApiResponseCallback<AccountGithubResponse>) : Disposable{
        return networkService.getDeas()
            .configured()
            .subscribes({ callback.onSuccess(it) }, { error: NetworkError -> callback.onError(error) })
    }

}