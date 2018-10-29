package com.example.yervand.ftstest.db

import android.content.Context
import com.example.yervand.ftstest.di.AppContext
import com.example.yervand.ftstest.di.DatabaseInfo
import org.sqlite.database.sqlite.SQLiteDatabase
import org.sqlite.database.sqlite.SQLiteOpenHelper
import javax.inject.Inject

class SQLiteDbHelper @Inject constructor(@AppContext context: Context,
                                         @DatabaseInfo databaseName: String,
                                         @DatabaseInfo databaseVersion: Int
) : SQLiteOpenHelper(context, databaseName, null, databaseVersion) {



    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL("CREATE VIRTUAL TABLE $TABLE_NAME_FTS_3 USING fts5 ($COL_NUMBER  ,$COL_NUMBER_STRING  , $COL_TEXT  , $COL_TYPE  )")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    companion object {
        const val TABLE_NAME_FTS_3 = "fts3_test"
        const val COL_NUMBER = "number"
        const val COL_NUMBER_STRING = "number_string"
        const val COL_TEXT = "text"
        const val COL_TYPE = "type"
        const val COL_ROW_ID = "rowid"
    }
}