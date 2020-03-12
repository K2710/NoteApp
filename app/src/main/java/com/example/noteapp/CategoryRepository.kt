package com.example.noteapp

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class CategoryRepository(context: Context) {
    private val appDatabase:AppDatabase
    init {
        appDatabase = AppDatabase.invoke(context)
    }

    fun getAll():LiveData<List<Category>>{
        return appDatabase.categoryDao().getAll()
    }
    fun saveCategory(nombre:String){
        val category = Category()
        category.nombre = nombre

        saveCategory(category)

    }
    fun saveCategory(category: Category){
        object  : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg params: Void?): Void? {
                appDatabase.categoryDao().saveCategory(category)
                return null
            }
        }.execute()
    }
}