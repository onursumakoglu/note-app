package com.task.noteapp.presentation

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.task.noteapp.R
import com.task.noteapp.data.local.entity.Note
import com.task.noteapp.databinding.FragmentNoteEditBinding
import com.task.noteapp.utils.getDate
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NoteEditFragment : Fragment(R.layout.fragment_note_edit) {

    private lateinit var binding: FragmentNoteEditBinding
    private val viewModel by sharedViewModel<NoteViewModel>()
    private val receivedArgs: NoteEditFragmentArgs by navArgs()
    private var newNote: Note? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNoteEditBinding.bind(view)

        binding.appBar.newNoteOrSaveIcon.setImageResource(R.drawable.ic_save_note)
        binding.appBar.newNoteOrSaveText.setText(R.string.save_text)

        listener()
        fillFields()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun listener() {
        binding.appBar.noteActionContainer.setOnClickListener {
            saveNote()
        }
    }

    private fun fillFields() {
        if (receivedArgs.note != null) {
            binding.title.setText(receivedArgs.note?.title.toString())
            binding.description.setText(receivedArgs.note?.description.toString())
            binding.imageUrl.setText(receivedArgs.note?.imageUrl.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveNote() {
        if (binding.title.text.toString().isEmpty()) {
            Toast.makeText(context, R.string.missing_title_warning, Toast.LENGTH_SHORT).show()
        } else {
            newNote = Note(
                title = binding.title.text.toString(),
                description = binding.description.text.toString(),
                imageUrl = binding.imageUrl.text.toString(),
                createdDate = getDate()
            )
            if (receivedArgs.note != null) {
                newNote?.let {
                    it.uid = receivedArgs.note?.uid
                    viewModel.updateNote(it)
                }
            } else {
                newNote?.let { viewModel.createNote(it) }
            }
            Toast.makeText(context, R.string.note_saved_text, Toast.LENGTH_SHORT).show()
            findNavController().popBackStack(R.id.noteListFragment, false)
        }
    }

}