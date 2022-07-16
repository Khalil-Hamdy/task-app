package com.khalil.taskapp.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.khalil.taskapp.R
import com.khalil.taskapp.databinding.ActivityCreateNoteBinding
import com.khalil.taskapp.dp.NotesDatabase
import com.khalil.taskapp.model.Note
import com.khalil.taskapp.repository.NoteRepository
import com.khalil.taskapp.viewModel.NoteViewModel
import com.khalil.taskapp.viewModel.NoteViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreateNoteBinding
    private var selectedImagePath = ""
    private lateinit var alreadyAvailableNote: Note

    private val REQUEST_CODE_STORAGE_PERMISSION = 1
    private val REQUEST_CODE_SELECT_IMAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageBack.setOnClickListener{
            startActivity(Intent(applicationContext, NoteActivity::class.java))
            this.finish()
            //onBackPressed()
//            val intent2 = Intent(this, NoteActivity::class.java)
//            startActivity(intent2)
//            finish()
        }

        binding.textDateTime.setText(
            SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                .format(Date())
        )


        binding.imageRemoveImage.setOnClickListener{
            binding.imageNote.setImageBitmap(null)
            binding.imageNote.visibility = View.GONE
            binding.imageRemoveImage.visibility = View.GONE
            selectedImagePath = ""
        }

        binding.addImage.setOnClickListener{
            selectImage()
        }

        //binding.imageSave.setOnClickListener { saveNote() }

//        if (intent.getBooleanExtra("isViewOrUpdate", false)) {
//            alreadyAvailableNote = intent.getSerializableExtra("note") as Note
//            setViewOrUpdateNote()
//        }

        //**************** new code *****************
        binding.imageNote.visibility = View.GONE
        binding.imageRemoveImage.visibility = View.GONE

        var noteId = -1
        val noteType = intent.getStringExtra("noteType")
        if (noteType.equals("Edit")) {
            // on below line we are setting data to edit text.
            val noteTitle = intent.getStringExtra("noteTitle")
             noteId = intent.getIntExtra("noteId",-1)
            val noteDataTime = intent.getStringExtra("noteDataTime")
            val noteSubtitle = intent.getStringExtra("noteSubtitle")

            val vaildNoteText = intent.getBooleanExtra("vnoteNoteText",false)
            if(vaildNoteText) {
                val noteNoteText = intent.getStringExtra("noteNoteText")
                binding.inputNote.setText(noteNoteText )
            }
            val vaildImagePass = intent.getBooleanExtra("vnoteImagePath",false)
            if(vaildImagePass) {
                val noteImagePath = intent.getStringExtra("noteSubtitle")
                binding.imageNote.setImageBitmap(BitmapFactory.decodeFile(noteImagePath))
                binding.imageNote.visibility = View.VISIBLE
                binding.imageRemoveImage.visibility = View.VISIBLE
                if (noteImagePath != null) {
                    selectedImagePath = noteImagePath
                }
            }
            else{
                binding.imageNote.visibility = View.GONE
                binding.imageRemoveImage.visibility = View.GONE
            }

            binding.inputNoteTitle.setText(noteTitle)
            binding.inputNoteSubtitle.setText(noteSubtitle)
            binding.textDateTime.setText(noteDataTime)
        }

        binding.imageSave.setOnClickListener {

            if(binding.inputNoteTitle.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_SHORT).show()
            }
            else if(binding.inputNoteSubtitle.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Note can't be empty!", Toast.LENGTH_SHORT).show()
            }
            else {
                if (noteType.equals("Edit")) {
                    if (binding.inputNoteTitle.text.toString().trim().isNotEmpty()
                        && binding.inputNoteSubtitle.text.toString().trim().isNotEmpty()
                    ) {
                        val note = Note(
                            binding.inputNoteTitle.text.toString(),
                            binding.textDateTime.text.toString(),
                            binding.inputNoteSubtitle.text.toString(),
                        )
                        note.id = noteId
                        note.noteText = binding.inputNote.text.toString()
                        note.imagePath = selectedImagePath

                        val database = NotesDatabase(this)
                        val repository = NoteRepository(database)
                        val factory = NoteViewModelFactory(repository)

                        val viewModel = ViewModelProvider(
                            this,
                            factory
                        ).get(NoteViewModel::class.java) // MANDATORY
                        viewModel.upsert(note)
                        Toast.makeText(this, "Note Updated..", Toast.LENGTH_LONG).show()
                    }
                } else {
                    if (binding.inputNoteTitle.text.toString().trim().isNotEmpty()
                        && binding.inputNoteSubtitle.text.toString().trim().isNotEmpty()
                    ) {
                        val note = Note(
                            binding.inputNoteTitle.text.toString(),
                            binding.textDateTime.text.toString(),
                            binding.inputNoteSubtitle.text.toString(),
                        )

                        note.noteText = binding.inputNote.text.toString()
                        note.imagePath = selectedImagePath

                        val database = NotesDatabase(this)
                        val repository = NoteRepository(database)
                        val factory = NoteViewModelFactory(repository)

                        val viewModel = ViewModelProvider(
                            this,
                            factory
                        ).get(NoteViewModel::class.java) // MANDATORY
                        viewModel.upsert(note)
                        Toast.makeText(this, "Note added..", Toast.LENGTH_LONG).show()
                    }
                }

                startActivity(Intent(applicationContext, NoteActivity::class.java))
                this.finish()
            }
        }


    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                val selectedImageUri = data.data
                try {
                    val inputStream = contentResolver.openInputStream(selectedImageUri!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.imageNote.setImageBitmap(bitmap)
                    binding.imageNote.setVisibility(View.VISIBLE)
                    findViewById<View>(R.id.imageRemoveImage).visibility = View.VISIBLE
                    selectedImagePath = getPathFromUri(selectedImageUri)!!
                } catch (exception: Exception) {
                    Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getPathFromUri(contentUri: Uri): String? {
        val filePath: String?
        val cursor = contentResolver
            .query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }


 }


