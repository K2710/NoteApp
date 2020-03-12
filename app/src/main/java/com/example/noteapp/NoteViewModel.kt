package com.example.noteapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NoteViewModel(context: Context) : ViewModel() {

    private val noteRepository:NoteRepository
    init {
        noteRepository = NoteRepository(context)
    }

    fun getAll():LiveData<List<NoteCategory>>{
        return noteRepository.getAll()
    }
}