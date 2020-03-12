package com.example.noteapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Transaction
    @Query("SELECT * FROM note where estado = 0")
    fun getAll(): LiveData<List<NoteCategory>>

    @Query("SELECT * FROM note where nodeID = :id")
    fun getById(id:Int):LiveData<Note>

    @Transaction
    @Query("SELECT * FROM note where estado = 1")
    fun getAllDeleted(): LiveData<List<NoteCategory>>

    @Transaction
    @Query("SELECT * FROM note where idCategory = :idCategory")
    fun getByCategory(idCategory:Int):LiveData<List<NoteCategory>>

    @Insert
    fun saveNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Update
    fun updateNote(note: Note)
}