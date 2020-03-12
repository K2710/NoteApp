package com.example.noteapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo(name = "nombre") var nombre:String
){
    constructor() : this(0, "")
}