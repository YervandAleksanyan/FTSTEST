package com.example.yervand.ftstest.db.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class Codex(@PrimaryKey var Id: Int = 0,
                 var CreatedAt: Date = Date(),
                 var Root: CodexEntity? = null,
                 var NumberOfObjects: Long = 0) : RealmObject()