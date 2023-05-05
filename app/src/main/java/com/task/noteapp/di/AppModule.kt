package com.task.noteapp.di

import com.task.noteapp.data.local.repository.NoteRepository
import com.task.noteapp.data.local.NoteDatabase
import com.task.noteapp.presentation.NoteViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { NoteDatabase.getDatabase(androidApplication().applicationContext).noteDao() }

    factory { NoteRepository(get()) }

    viewModel { NoteViewModel(get()) }
}