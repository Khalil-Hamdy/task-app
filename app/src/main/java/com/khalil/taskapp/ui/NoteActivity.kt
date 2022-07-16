package com.khalil.taskapp.ui

import android.content.Intent
import android.os.Bundle

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.khalil.taskapp.databinding.ActivityNoteBinding
import com.khalil.taskapp.dp.NotesDatabase
import com.khalil.taskapp.model.Note
import com.khalil.taskapp.model.NoteListener
import com.khalil.taskapp.repository.NoteRepository
import com.khalil.taskapp.viewModel.NoteViewModel
import com.khalil.taskapp.viewModel.NoteViewModelFactory

class NoteActivity : AppCompatActivity(),NoteListener {

    lateinit var binding: ActivityNoteBinding

    val REQUEST_CODE_FOR_RESULT : Int = 1
    val REQUEST_CODE_UPDATE_NOTE : Int = 2
    val REQUEST_CODE_show_NOTE : Int = 3


    //lateinit var notesRecyclerView: RecyclerView
    lateinit var noteList: List<Note>
    val notesAdapter = NotesAdapter(this, this)

    private var noteClickedListener = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageAddNoteMain.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    CreateNoteActivity::class.java
                )
            )
            this.finish()
        })

        binding.imageAddNote.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    CreateNoteActivity::class.java
                )
            )
            this.finish()
        }

//        binding.inputSearch.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//
//            }
//            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//                notesAdapter.cancelTimer()
//            }
//
//            override fun afterTextChanged(editable: Editable) {
//                if (noteList.size != 0) {
//                    notesAdapter.searchNote(editable.toString())
//                }
//            }
//        })

        binding.noteRecyclerview.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noteRecyclerview.adapter = notesAdapter

        //new code
        val database = NotesDatabase(this)
        val repository = NoteRepository(database)
        val factory = NoteViewModelFactory(repository)

        val viewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java) // MANDATORY
        viewModel.getAllNote().observe(this, Observer  {
            notesAdapter.updateList(it)
        })



    }


    override fun onNoteClicked(note: Note, position: Int) {
        noteClickedListener = position
        val intent = Intent(applicationContext, CreateNoteActivity::class.java)
        intent.putExtra("noteType", "Edit")
        //intent.putExtra("isViewOrUpdate", true)
        intent.putExtra("noteId", note.id)
        intent.putExtra("noteDataTime", note.dataTime )
        intent.putExtra("noteSubtitle", note.subtitle)
        if(note.noteText.trim().isNotEmpty() ){
                intent.putExtra("vnoteNoteText", true)
                intent.putExtra("noteNoteText", note.noteText)
            }
        intent.putExtra("noteTitle", note.title)
        if( note.imagePath.trim().isNotEmpty() ) {
            intent.putExtra("vnoteImagePath", true)
            intent.putExtra("noteImagePath", note.imagePath)
        }

        //intent.putExtra("note", note)
        startActivity(intent)
        this.finish()
    }

}