package com.test.tiketchallenge.di

import com.readystatesoftware.chuck.internal.ui.MainActivity
import com.test.tiketchallenge.github.GithubUserActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

//    @ContributesAndroidInjector(modules = [(GithubUserActivity::class)])
//    internal abstract fun bindSplashActivity(): GithubUserActivity

}