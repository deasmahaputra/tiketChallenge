package com.test.tiketchallenge.di

import android.app.Application
import com.test.tiketchallenge.base.TiketApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidInjectionModule::class), (AppModule::class), (NetworkModule::class),
    (ActivityBuilder::class),(FragmentBuilder::class)])

interface AppComponent {

    fun inject(app: TiketApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}