package com.example.yervand.ftstest.di.modules.main

import com.example.yervand.ftstest.SearchApp
import javax.inject.Singleton
import dagger.Provides
import com.example.yervand.ftstest.di.DatabaseInfo
import dagger.Module
import io.realm.Realm
import org.sqlite.database.sqlite.SQLiteOpenHelper


@Module
class DbModule {

    @Provides
    @DatabaseInfo
    fun provideDatabaseName(context: SearchApp): String = context.getDatabasePath("fts_test").path

    @Provides
    @DatabaseInfo
    fun provideDatabaseVersion() = 1


    @Provides
    @Singleton
    fun provideDefaultRealm(): Realm {
        return Realm.getDefaultInstance()
    }

    @Provides
    @Singleton
    fun provideDBHelper(helper: SQLiteOpenHelper) = helper
}