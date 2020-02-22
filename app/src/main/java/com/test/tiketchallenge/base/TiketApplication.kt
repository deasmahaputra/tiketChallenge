package com.test.tiketchallenge.base

import android.app.Activity
import android.app.Application
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import com.test.tiketchallenge.di.AppComponent
import com.test.tiketchallenge.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class TiketApplication : Application(), HasActivityInjector {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var activityDispatchingAndroidInjector : DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityDispatchingAndroidInjector
    }


    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().build())
        DaggerAppComponent.builder().application(this).build().inject(this)

    }

}