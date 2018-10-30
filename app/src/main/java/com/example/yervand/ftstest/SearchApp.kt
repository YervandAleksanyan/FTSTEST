package com.example.yervand.ftstest

import com.example.yervand.ftstest.di.components.DaggerAppComponent
import com.getkeepsafe.relinker.ReLinker
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.realm.Realm
import io.realm.RealmConfiguration


class SearchApp : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        ReLinker.loadLibrary(this, "sqliteX")
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
                .assetFile("Codex.realm")
                .build())
    }

    override fun applicationInjector(): AndroidInjector<out SearchApp> = DaggerAppComponent.builder().create(this)
}