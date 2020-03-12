package com.example.noteapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note (
    @PrimaryKey(autoGenerate = true) var nodeID:Int,
    @ColumnInfo(name = "nodeName") var nodeName:String,
    @ColumnInfo(name = "nodeDes") var nodeDes:String,
    @ColumnInfo(name = "date") var date:String,
    @ColumnInfo(name = "color") var color:String,
    @ColumnInfo(name = "estado") var estado:Int,
    @ColumnInfo(name = "idCategory") var idCategory:Int
){
    constructor() : this(0,"","","", "", 0, 0)
}