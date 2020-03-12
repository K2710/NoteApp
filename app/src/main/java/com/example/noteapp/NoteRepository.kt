package com.example.noteapp

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import java.text.SimpleDateFormat
import java.util.*

class NoteRepository(context: Context) {
    private var appDatabase:AppDatabase

    init {
        appDatabase = AppDatabase.invoke(context)
    }
    fun insertNote(title:String, description:String,idCategory:Int){
        val note = Note()
        note.nodeName = title
        note.nodeDes = description
        note.date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        note.estado = 0
        note.idCategory = idCategory
        insertNote(note)
    }
    fun insertNote(note: Note){
        object : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg params: Void?): Void? {
                appDatabase.noteDao().saveNote(note)

                return null
            }

        }.execute()

    }

    fun updateNote(id:Int,title:String, description:String, estado:Int = 0, idCategory:Int){
        val note = Note()
        note.nodeID = id
        note.nodeName = title
        note.nodeDes = description
        note.date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        note.estado = estado
        note.idCategory = idCategory
        updateNote(note)
    }
    fun updateNote(note: Note){
        object : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg params: Void?): Void? {
                appDatabase.noteDao().updateNote(note)

                return null
            }

        }.execute()

    }

    fun getAll():LiveData<List<NoteCategory>>{
        return appDatabase.noteDao().getAll()
    }
    fun getNote(id:Int):LiveData<Note>{
        return appDatabase.noteDao().getById(id)
    }
    fun getAllDeleted():LiveData<List<NoteCategory>>{
        return appDatabase.noteDao().getAllDeleted()
    }

    fun delete(note: Note){
        object : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg params: Void?): Void? {
                appDatabase.noteDao().deleteNote(note)

                return null
            }

        }.execute()
    }

}