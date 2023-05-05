package com.task.noteapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.task.noteapp.data.local.dao.NoteDao
import com.task.noteapp.data.local.entity.Note

@Database(entities = [Note::class], version = 3)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var noteDatabaseInstance: NoteDatabase? = null

        fun getDatabase(context: Context) = noteDatabaseInstance ?: synchronized(Any()) {
            noteDatabaseInstance ?: createDatabase(context).also {
                noteDatabaseInstance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, NoteDatabase::class.java, "note_database_3").build()

    }
}