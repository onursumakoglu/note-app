package com.task.noteapp.presentation

import androidx.lifecycle.*
import com.task.noteapp.data.local.entity.Note
import com.task.noteapp.data.local.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel constructor(private val noteRepository: NoteRepository) : ViewModel() {

    private var _noteList = mutableListOf<Note>()
    var noteList = MutableLiveData<List<Note>>()
        private set

    fun getAllNotes() = viewModelScope.launch {
        _noteList = noteRepository.getAllNotes().toMutableList()
        noteList.value = _noteList
    }

    fun createNote(note: Note) = viewModelScope.launch {
        noteRepository.insertNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        noteRepository.deleteNote(note)
        getAllNotes()
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        noteRepository.updateNote(note)
    }

}
