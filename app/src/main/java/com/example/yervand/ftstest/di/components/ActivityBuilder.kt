package com.example.yervand.ftstest.di.components

import com.example.yervand.ftstest.view.SearchActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector()
    abstract fun bindSearchActivity(): SearchActivity
}