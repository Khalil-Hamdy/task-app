package com.khalil.taskapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khalil.taskapp.repository.NoteRepository

class NoteViewModelFactory (private val repository: NoteRepository)
    :ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(repository) as T
    }

}