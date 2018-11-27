package com.example.yervand.ftstest.di.modules.viewmodels

import androidx.appcompat.app.AppCompatActivity
import com.example.yervand.ftstest.viewmodel.SearchViewModel

interface IViewModelProvider {
    fun getSearchViewModel(context: AppCompatActivity): SearchViewModel
}