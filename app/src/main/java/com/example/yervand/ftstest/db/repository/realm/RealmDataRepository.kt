package com.example.yervand.ftstest.db.repository.realm

import com.example.yervand.ftstest.db.model.CodexEntity
import com.example.yervand.ftstest.db.model.CodexEntityType
import io.realm.Realm
import javax.inject.Inject

class RealmDataRepository @Inject constructor(private val realm: Realm) : RealmRepository {
    override fun all(): List<CodexEntity> = realm
            .where(CodexEntity::class.java)
            .findAll()

    companion object {
        const val NUMBER = "Number"
    }
}