package com.example.yervand.ftstest.db.repository.realm

import com.example.yervand.ftstest.db.model.CodexEntity

interface RealmRepository {
    fun all(): List<CodexEntity>
}