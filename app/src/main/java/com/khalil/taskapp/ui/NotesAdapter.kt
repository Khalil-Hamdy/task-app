package com.khalil.taskapp.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khalil.taskapp.databinding.ItemContainerNoteBinding
import com.khalil.taskapp.model.Note
import com.khalil.taskapp.model.NoteListener
import java.util.*

class NotesAdapter(val context: Context, private val noteListener: NoteListener) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val notes = ArrayList<Note>()
    private var timer = Timer()
    private val noteSource = ArrayList<Note>()


    inner class NoteViewHolder(val binding: ItemContainerNoteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setNote(note: Note) {
            binding.textTitle.setText(note.title)
            if (note.subtitle.trim().isEmpty()) {
                binding.textSubtitle.setVisibility(View.GONE)
            } else {
                binding.textSubtitle.setText(note.subtitle)
            }
            binding.textDateTime.setText(note.dataTime)

            if (note.imagePath.trim().isNotEmpty()) {
                binding.imageNote.setImageBitmap(BitmapFactory.decodeFile(note.imagePath))
                binding.imageNote.visibility = View.VISIBLE
            } else {
                binding.imageNote.visibility = View.GONE
            }
        }

    }
    fun searchNote(searchKeyword: String) {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                if (searchKeyword.trim { it <= ' ' }.isEmpty()) {
                    updateList(noteSource)
                } else {
                    val temp = ArrayList<Note>()
                    for (note in noteSource!!) {
                        if (note.title.toLowerCase()
                                .contains(searchKeyword.lowercase(Locale.getDefault()))
                            || note.subtitle.toLowerCase()
                                .contains(searchKeyword.lowercase(Locale.getDefault()))
                            || note.noteText.toLowerCase()
                                .contains(searchKeyword.lowercase(Locale.getDefault()))
                        ) {
                            temp.add(note)
                        }
                    }
                    updateList(temp)
                }
                Handler(Looper.getMainLooper()).post {
                    run { notifyDataSetChanged() }
                }
            }
        }, 500)
    }

    fun cancelTimer() {
        if (timer != null) {
            timer!!.cancel()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(ItemContainerNoteBinding.inflate(LayoutInflater
            .from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.setNote(notes[position])
        holder.binding.layoutNote.setOnClickListener(View.OnClickListener {
            noteListener.onNoteClicked(
                notes[position],
                position
            )
        })
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun updateList(newList: List<Note>) {

        notes.clear()
        notes.addAll(newList)
        notifyDataSetChanged()
    }

}