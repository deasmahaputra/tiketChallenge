package com.test.tiketchallenge.github

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.PublishRelay
import com.test.tiketchallenge.base.BaseViewModel
import com.test.tiketchallenge.extension.configured
import com.test.tiketchallenge.network.ApiService
import com.test.tiketchallenge.network.NetworkError
import com.test.tiketchallenge.network.QueryPageRequest
import com.test.tiketchallenge.network.response.AccountGithubResponse
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.io.InterruptedIOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class GithubUserViewModel @Inject constructor(private var apiService : ApiService) : BaseViewModel<GithubUserContract>(apiService){

    private val autoCompletePublishSubject = PublishRelay.create<QueryPageRequest>()
    var githubAccount : MutableLiveData<AccountGithubResponse> = MutableLiveData()
    var isGithubAccountSearching : MutableLiveData<Boolean> = MutableLiveData()
    private val composite = CompositeDisposable()
    private var tempQuery = ""
    private var page = 0

    fun fetchGithubAccount(){
            val disposable = autoCompletePublishSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .switchMap{
                    if(it.query?.length!! < 3 || (TextUtils.isDigitsOnly(it.query) && it.query?.length!! < 12)){
                        Observable.just(AccountGithubResponse())
                    } else if (!TextUtils.isDigitsOnly(it.query)) apiService.fetchGithubAccount(it.query?:"", it.page?:0)
                    else apiService.fetchGithubAccount(it.query?:"", it.page?:0)

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
        var queryPage = QueryPageRequest(query, page)
        autoCompletePublishSubject.accept(queryPage)
        isGithubAccountSearching.value = true
        //fetchGithubAccount(page, query)
    }
}
