package com.example.yervand.ftstest.db.repository.sqlite

import com.example.yervand.ftstest.db.model.CodexEntity
import io.reactivex.Single

interface FTSRepository {
    fun insert(items: List<CodexEntity>)

    fun search(key: String): Single<List<CodexEntity>>?
}