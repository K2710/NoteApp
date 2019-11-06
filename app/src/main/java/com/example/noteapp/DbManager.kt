package com.example.noteapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager(context: Context) {

    //database name
    var dbName = "MyNotes"
    //table name
    var dbTable = "Note"
    //columns
    var colID = "ID"
    var colTitle = "Title"
    var colDes = "Description"
    //database version
    var dbVersion = 1

    // CREATE TABLE IF NOT EXISTS MyNotes (ID INTEGER PRIMARY KEY,title TEXT, Description TEXT);"
    val sqlCreateTable =
        "CREATE TABLE IF NOT EXISTS $dbTable ($colID INTEGER PRIMARY KEY AUTOINCREMENT,$colTitle TEXT, $colDes TEXT)"

    var sqlDB:SQLiteDatabase? = null

    init {
        val db = DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHelperNotes(context: Context) :
        SQLiteOpenHelper(context, dbName, null, dbVersion) {
        private var context: Context? = context

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context, "database created...", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table if Exists $dbTable")
        }
    }

    fun insert (values:ContentValues): Long{
        val ID = sqlDB!!.insert(dbTable, "", values)
        return ID
    }

    fun query(projection:Array<String>, selection: String, selectionArgs:Array<String>, sorOrder:String):Cursor{
        val qb = SQLiteQueryBuilder();
        qb.tables = dbTable
        val cursor = qb.query (sqlDB, projection, selection, selectionArgs, null, null, sorOrder)
        return cursor
    }

    fun delete (selection:String, selectionArgs: Array<String>):Int{
        val count = sqlDB!!.delete(dbTable, selection, selectionArgs)
        return count
    }

    fun update (values: ContentValues, selection: String, selectionArgs: Array<String>):Int{
        val count = sqlDB!!.update(dbTable, values, selection, selectionArgs)
        return count
    }
}