package com.test.tiketchallenge.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.tiketchallenge.network.ApiService
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

abstract class BaseViewModel<N>(apiService: ApiService) : ViewModel(){

    private lateinit var navigator: WeakReference<N>
    var isLoading = ObservableBoolean(false)
    val compositeDisposable = CompositeDisposable()

    var isLoadingMutable: MutableLiveData<Boolean> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun getIsLoading() : ObservableBoolean{
        return isLoading
    }

    fun setNavigator(navigator: N) {
        this.navigator = WeakReference(navigator)
    }

    fun getNavigator(): N = navigator.get()!!
}