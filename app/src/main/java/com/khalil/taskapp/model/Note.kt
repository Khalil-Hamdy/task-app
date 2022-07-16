package com.khalil.taskapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "note")
data class Note (
    @ColumnInfo(name = "title") var title : String,
    @ColumnInfo(name = "data_time") var dataTime : String,
    @ColumnInfo(name = "subtitle") var subtitle : String
    ){
    @PrimaryKey(autoGenerate = true) var id: Int? = null
    @ColumnInfo(name = "note_text")
    lateinit var noteText : String
    @ColumnInfo(name = "image_path")
    lateinit var imagePath : String

}