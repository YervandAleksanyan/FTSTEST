package com.example.yervand.ftstest.db.model

import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.Ignore
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*


open class CodexEntity(@PrimaryKey var Id: Long = 0,
                       var CreatedAt: Date = Date(),
                       var Number: Double = 0.0,
                       var NumberString: String? = null,
                       var HasWarning: Boolean = false,
                       var Text: String? = null,
                       var Type: Int = 0,
                       var Parent: CodexEntity? = null,
                       @LinkingObjects("Parent")
                       val Children: RealmResults<CodexEntity?>? = null,
                       var RelatedAreaId: Int? = 0) : RealmObject() {
}