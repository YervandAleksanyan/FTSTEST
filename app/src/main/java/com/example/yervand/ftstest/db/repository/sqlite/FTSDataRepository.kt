package com.example.yervand.ftstest.db.repository.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.yervand.ftstest.db.SQLiteDbHelper
import com.example.yervand.ftstest.db.model.CodexEntity
import com.example.yervand.ftstest.di.AppContext
import com.example.yervand.ftstest.utill.QueryBuilder
import io.reactivex.Single
import org.sqlite.database.sqlite.SQLiteDatabase
import java.util.ArrayList
import javax.inject.Inject

class FTSDataRepository @Inject constructor(
) : FTSRepository {

    @Inject
    lateinit var dbHelper: SQLiteDbHelper

    override fun insert(items: List<CodexEntity>) {
        checkIsTableNotEmpty(dbHelper) {
                var rowsInserted = -1
                val database: SQLiteDatabase = dbHelper.writableDatabase
                database.beginTransaction()
                try {
                    for (entity in items) {
                        val values = ContentValues()
                        values.put(SQLiteDbHelper.COL_NUMBER, entity.Number)
                        values.put(SQLiteDbHelper.COL_NUMBER_STRING, entity.NumberString)
                        values.put(SQLiteDbHelper.COL_TEXT, entity.Text)
                        values.put(SQLiteDbHelper.COL_TYPE, entity.Type)
                        database.insert(SQLiteDbHelper.TABLE_NAME_FTS_3, null, values)
                    }
                    database.setTransactionSuccessful() //TRANSACTION SUCCESSFUL
                    rowsInserted = items.size
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                Log.i("test", "$rowsInserted")
                database.endTransaction()
            }
        }
    }

    override fun search(key: String): Single<List<CodexEntity>>? {
        val entities = ArrayList<CodexEntity>()
        if (!key.isEmpty()) {
            val database: SQLiteDatabase = dbHelper.readableDatabase
            val query = "SELECT ${SQLiteDbHelper.COL_NUMBER},${SQLiteDbHelper.COL_NUMBER_STRING},${SQLiteDbHelper.COL_TYPE}, highlight(${SQLiteDbHelper.TABLE_NAME_FTS_3}, 2, '<b><font color= blue>', '</font></b>') AS ${SQLiteDbHelper.COL_TEXT} FROM ${SQLiteDbHelper.TABLE_NAME_FTS_3} WHERE ${SQLiteDbHelper.COL_TEXT} MATCH ? ORDER BY ${SQLiteDbHelper.COL_NUMBER}"
            val cursor: Cursor? = database.rawQuery(query, arrayOf(QueryBuilder.createQuery(key)))
            if (cursor?.count!! > 0) {
                cursor.moveToFirst()
                (0.until(cursor.count)).forEach { _ ->
                    val entity = CodexEntity()
                    entity.Text = (cursor.getString(cursor.getColumnIndex(SQLiteDbHelper.COL_TEXT)))
                    entity.Number = (cursor.getDouble(cursor.getColumnIndex(SQLiteDbHelper.COL_NUMBER)))
                    entity.NumberString = (cursor.getString(cursor.getColumnIndex(SQLiteDbHelper.COL_NUMBER_STRING)))
                    entity.Type = (cursor.getInt(cursor.getColumnIndex(SQLiteDbHelper.COL_TYPE)))
                    entities.add(entity)
                    cursor.moveToNext()
                }
            }
            cursor.close()
        }
        return Single.just(entities)
    }


    private inline fun checkIsTableNotEmpty(dbHelper: SQLiteDbHelper, action: () -> Unit) {
        val database: SQLiteDatabase = dbHelper.writableDatabase
        val cursor = database.rawQuery("SELECT * FROM ${SQLiteDbHelper.TABLE_NAME_FTS_3}", null)
        if (cursor.count == 0) {
            action()
            cursor.close()
        }
    }
}