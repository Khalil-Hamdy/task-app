package com.khalil.taskapp.repository

import com.khalil.taskapp.dp.NotesDatabase
import com.khalil.taskapp.model.Note

class NoteRepository(private val dp:NotesDatabase) {
    fun upsert(item : Note) = dp.getNoteDao().upsert(item)
    fun delete(item : Note) = dp.getNoteDao().delete(item)
    fun getAllNote() = dp.getNoteDao().getAllNote()
}