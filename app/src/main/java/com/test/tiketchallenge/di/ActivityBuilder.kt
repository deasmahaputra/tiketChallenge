package com.test.tiketchallenge.di

import com.test.tiketchallenge.di.module.GithubModule
import com.test.tiketchallenge.github.GithubUserActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [(GithubModule::class)])
    internal abstract fun bindGithubUser(): GithubUserActivity

}