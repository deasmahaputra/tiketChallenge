package com.test.tiketchallenge.di.module

import androidx.lifecycle.ViewModelProvider
import com.test.tiketchallenge.base.ViewModelProviderFactory
import com.test.tiketchallenge.github.GithubUserViewModel
import com.test.tiketchallenge.network.ApiService
import dagger.Module
import dagger.Provides

@Module
class GithubModule {

//    @Provides
//    internal fun provideGithubModule(apiService: ApiService): GithubUserViewModel {
//        return GithubUserViewModel(apiService)
//    }
//
//    @Provides
//    internal fun githubModelProvider(githubModel: GithubUserViewModel): ViewModelProvider.Factory {
//        return ViewModelProviderFactory(githubModel)
//    }

    @Provides
    internal fun provideGithubModule(apiService: ApiService) : GithubUserViewModel{
        return GithubUserViewModel(apiService)
    }

    @Provides
    internal fun githubModelModule(githubUserViewModel: GithubUserViewModel) : ViewModelProvider.Factory {
        return ViewModelProviderFactory(githubUserViewModel)
    }

}