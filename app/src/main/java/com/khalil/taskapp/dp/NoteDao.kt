package com.khalil.taskapp.dp

import androidx.lifecycle.LiveData
import androidx.room.*
import com.khalil.taskapp.model.Note


@Dao
interface NoteDao {

    @Query("SELECT * FROM note ORDER BY id DESC")
    fun getAllNote(): LiveData<List<Note>>

    @Delete
    fun delete(item: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(item: Note)

}