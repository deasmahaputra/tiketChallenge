package com.test.tiketchallenge.github

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.PublishRelay
import com.test.tiketchallenge.base.BaseViewModel
import com.test.tiketchallenge.extension.configured
import com.test.tiketchallenge.network.ApiResponseCallback
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
    private var githubAccount : MutableLiveData<AccountGithubResponse> = MutableLiveData()
    private val composite = CompositeDisposable()

    fun fetchGithubAccount(){

            val disposable = autoCompletePublishSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .switchMap{
                    if(it.length < 2 || (TextUtils.isDigitsOnly(it) && it.length < 12)){
                        Observable.just(AccountGithubResponse())
                    } else if (!TextUtils.isDigitsOnly(it)) apiService.fetchGithubAccount(it)
                    else apiService.fetchGithubAccount( it)
                }.configured().doOnNext {
                    githubAccount.value = AccountGithubResponse()
                    Log.d("TESTTTT $$", it.total_count.toString())
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
                    Log.d("TESTTTT ##", result.total_count.toString())
                }, { t: Throwable ->
                    val error = NetworkError(t)
                    errorMessage.value = error.getErrorMessage()
                    githubAccount.value = AccountGithubResponse()

                })

        composite.add(disposable)

    }

    fun onInputStateChanged(query: String) {
        autoCompletePublishSubject.accept(query)
    }

    fun getDeas(){
        val disposable = apiService.getData(object : ApiResponseCallback<AccountGithubResponse>{
            override fun onSuccess(response: AccountGithubResponse) {
                Log.d("TESTTTT", response.total_count.toString())
            }

            override fun onError(error: NetworkError) {
                Log.d("TESTTTT", "ERROR")
            }

        })

        composite.add(disposable)
    }



}
