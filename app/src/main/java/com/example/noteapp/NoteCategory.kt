package com.example.noteapp

import androidx.room.Embedded
import androidx.room.Relation


data class NoteCategory(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "idCategory",
        entityColumn = "id"
    )
    val category: Category
)