package com.task.noteapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.task.noteapp.data.local.entity.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    suspend fun getAllNotes(): List<Note>

    @Insert
    suspend fun insertNote(vararg note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)
}