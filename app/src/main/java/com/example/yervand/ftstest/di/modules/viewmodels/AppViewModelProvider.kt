package com.example.yervand.ftstest.di.modules.viewmodels

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.yervand.ftstest.di.factories.SearchViewModelFactory
import com.example.yervand.ftstest.viewmodel.SearchViewModel
import javax.inject.Inject

class AppViewModelProvider @Inject constructor(var factory: SearchViewModelFactory) : IViewModelProvider {
    override fun getSearchViewModel(context: AppCompatActivity): SearchViewModel {
        return ViewModelProviders.of(context, factory)[SearchViewModel::class.java]
    }
}