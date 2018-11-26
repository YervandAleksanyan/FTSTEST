package com.example.yervand.ftstest.di.modules.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yervand.ftstest.view.AppViewModelProvider
import com.example.yervand.ftstest.view.IViewModelProvider
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

    @Binds
    internal abstract fun bindViewModelProvider(viewModelProvider: AppViewModelProvider): IViewModelProvider
}