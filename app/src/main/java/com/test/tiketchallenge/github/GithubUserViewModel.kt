package com.test.tiketchallenge.github

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.PublishRelay
import com.test.tiketchallenge.base.BaseViewModel
import com.test.tiketchallenge.extension.configured
import com.test.tiketchallenge.network.ApiService
import com.test.tiketchallenge.network.response.AccountGithubResponse
import io.reactivex.Observable
import java.io.InterruptedIOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GithubUserViewModel @Inject constructor(val apiService : ApiService) : BaseViewModel<GithubUserContract>(apiService){

    private val autoCompletePublishSubject = PublishRelay.create<String>()
    var githubAccount : MutableLiveData<AccountGithubResponse> = MutableLiveData()

    fun fetchGithubAccount(){
        compositeDisposable.add(
            autoCompletePublishSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .switchMap{
                    if(it.length < 3 || (TextUtils.isDigitsOnly(it) && it.length < 12)){
                        Observable.just(AccountGithubResponse())
                    } else if (!TextUtils.isDigitsOnly(it)) apiService.fetchGithubAccount(it)
                    else apiService.fetchGithubAccount( it)
                }.configured().doOnNext {
                    githubAccount.value = AccountGithubResponse()
                }
                .retryWhen { errors ->
                    errors.flatMap { error ->
                        if (error is InterruptedIOException){
                            Observable.just(AccountGithubResponse())
                        } else Observable.error(error)
                    }
                }
                .subscribe({ result ->
                    githubAccount.value = result
                }, { t: Throwable ->
                    //val error = NetworkError(t)
                    //errorMessage.value = error.getErrorMessageV2()
                    githubAccount.value = AccountGithubResponse()

                })
        )
    }

    fun onInputStateChanged(query: String) {
        autoCompletePublishSubject.accept(query)
    }

}
