package com.khalil.taskapp.viewModel

import androidx.lifecycle.ViewModel
import com.khalil.taskapp.model.Note
import com.khalil.taskapp.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository)  : ViewModel() {

    fun upsert(item: Note) = CoroutineScope(Dispatchers.IO).launch {
        repository.upsert(item)
    }
    fun delete(item: Note) = CoroutineScope(Dispatchers.IO).launch {
        repository.delete(item)
    }
    fun getAllNote() = repository.getAllNote()

}