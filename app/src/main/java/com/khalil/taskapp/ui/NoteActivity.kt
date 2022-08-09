package com.khalil.taskapp.ui

import android.content.Intent
import android.os.Bundle

import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.khalil.taskapp.databinding.ActivityNoteBinding
import com.khalil.taskapp.dp.NotesDatabase
import com.khalil.taskapp.model.Note
import com.khalil.taskapp.model.NoteListener
import com.khalil.taskapp.repository.NoteRepository
import com.khalil.taskapp.viewModel.NoteViewModel
import com.khalil.taskapp.viewModel.NoteViewModelFactory
import java.util.*
import kotlin.collections.ArrayList

class NoteActivity : AppCompatActivity(),NoteListener {

    lateinit var binding: ActivityNoteBinding

    lateinit var noteList: ArrayList<Note>
    lateinit var noteList2: ArrayList<Note>
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

        //new code
        val database = NotesDatabase(this)
        val repository = NoteRepository(database)
        val factory = NoteViewModelFactory(repository)

        val viewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java) // MANDATORY
        viewModel.getAllNote().observe(this, Observer  {
            notesAdapter.updateList(it)
            noteList2 = it as ArrayList<Note> /* = java.util.ArrayList<com.khalil.taskapp.model.Note> */
        })


        noteList = notesAdapter.getList()
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                var deleteNote : Note = noteList[position]
                noteList.removeAt(position)
                viewModel.delete(deleteNote)
                //notesAdapter.updateList(noteList)
                notesAdapter.notifyItemRemoved(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.noteRecyclerview)



        binding.noteRecyclerview.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noteRecyclerview.adapter = notesAdapter


        binding.inputSearch.setOnQueryTextListener( object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                var tempArr = ArrayList<Note>()
                for(arr in noteList2){
                    if(arr.title!!.toLowerCase(Locale.getDefault()).contains(p0.toString())){
                        tempArr.add(arr)
                    }
                }
                notesAdapter.updateList(tempArr)
                return true
            }

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
        if(!note.imagePath.isNullOrEmpty()) {
            intent.putExtra("vnoteImagePath", true)
            intent.putExtra("noteImagePath", note.imagePath)
        }

        //intent.putExtra("note", note)
        startActivity(intent)
        this.finish()
    }

}