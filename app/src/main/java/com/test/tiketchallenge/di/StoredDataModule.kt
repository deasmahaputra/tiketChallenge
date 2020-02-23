package com.test.tiketchallenge.di

import android.content.Context
import com.test.tiketchallenge.stored.PreferencesManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StoredDataModule {

    @Provides
    @Singleton
    fun providesPreferencesManager(context: Context): PreferencesManager = PreferencesManager(context)
}