package com.example.yervand.ftstest.di.components

import com.example.yervand.ftstest.SearchApp
import com.example.yervand.ftstest.di.modules.main.AppModule
import com.example.yervand.ftstest.di.modules.main.DbModule
import com.example.yervand.ftstest.di.modules.viewmodels.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules =
[AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityBuilder::class,
    ViewModelModule::class,
    DbModule::class])
interface AppComponent : AndroidInjector<SearchApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<SearchApp>()
}