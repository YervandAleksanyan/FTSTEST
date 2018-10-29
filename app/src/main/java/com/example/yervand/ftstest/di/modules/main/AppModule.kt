package com.example.yervand.ftstest.di.modules.main

import android.content.Context
import com.example.yervand.ftstest.SearchApp
import com.example.yervand.ftstest.di.AppContext
import dagger.Module
import dagger.Provides


@Module
class AppModule {
    @Provides
    @AppContext
    fun provideContext(application: SearchApp): Context {
        return application
    }
}