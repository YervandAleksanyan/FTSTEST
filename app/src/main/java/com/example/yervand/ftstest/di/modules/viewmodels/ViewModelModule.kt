package com.example.yervand.ftstest.di.modules.viewmodels

import androidx.lifecycle.ViewModel
import com.example.yervand.ftstest.viewmodel.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun bindSearchViewModel(weatherViewModel: SearchViewModel): ViewModel
}