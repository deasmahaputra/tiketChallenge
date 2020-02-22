package com.test.tiketchallenge.di.module

import androidx.lifecycle.ViewModelProvider
import com.test.tiketchallenge.base.ViewModelProviderFactory
import com.test.tiketchallenge.github.GithubUserViewModel
import com.test.tiketchallenge.network.ApiService
import dagger.Module
import dagger.Provides

@Module
class GithubModule {

    @Provides
    internal fun provideHomeModule(apiService: ApiService): GithubUserViewModel {
        return GithubUserViewModel(apiService)
    }

    @Provides
    internal fun homeModelProvider(githubModel: GithubUserViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(githubModel)
    }

}