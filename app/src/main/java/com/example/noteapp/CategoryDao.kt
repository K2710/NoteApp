package com.example.noteapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getAll():LiveData<List<Category>>

    @Insert
    fun saveCategory(category: Category)

    @Insert
    fun saveCategories(categories:List<Category>)
}