package com.khalil.taskapp.model

interface NoteListener {

    fun onNoteClicked(note: Note, position: Int)
}