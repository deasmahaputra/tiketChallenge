package com.test.tiketchallenge.github

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.PublishRelay
import com.test.tiketchallenge.base.BaseViewModel
import com.test.tiketchallenge.extension.configured
import com.test.tiketchallenge.network.ApiService
import com.test.tiketchallenge.network.NetworkError
import com.test.tiketchallenge.network.response.AccountGithubResponse
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.io.InterruptedIOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class GithubUserViewModel @Inject constructor(private var apiService : ApiService) : BaseViewModel<GithubUserContract>(apiService){

    private val autoCompletePublishSubject = PublishRelay.create<String>()
    var githubAccount : MutableLiveData<AccountGithubResponse> = MutableLiveData()
    var isGithubAccountSearching : MutableLiveData<Boolean> = MutableLiveData()
    private val composite = CompositeDisposable()

    private fun fetchGithubAccount(page : Int, query : String){
        autoCompletePublishSubject.accept(query)
            val disposable = autoCompletePublishSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .switchMap{
                    if(it.length < 3 || (TextUtils.isDigitsOnly(it))){
                        Observable.just(AccountGithubResponse())
                    } else if (!TextUtils.isDigitsOnly(it)) apiService.fetchGithubAccount(it, page)
                    else apiService.fetchGithubAccount( it, page)
                }.configured().doOnNext {
                    isGithubAccountSearching.value = true
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
                    isGithubAccountSearching.value = false
                    githubAccount.value = result
                }, { t: Throwable ->
                    isGithubAccountSearching.value = false
                    val error = NetworkError(t)
                    errorMessage.value = error.getErrorMessage()
                    githubAccount.value = AccountGithubResponse()
                    getNavigator().errorConnection()
                })

        composite.add(disposable)

    }

    fun onInputStateChanged(query: String, page: Int) {
        isGithubAccountSearching.value = true
        fetchGithubAccount(page, query)
    }
}
