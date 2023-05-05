package com.task.noteapp.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.task.noteapp.R
import com.task.noteapp.data.local.entity.Note
import com.task.noteapp.databinding.FragmentNoteListBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NoteListFragment : Fragment(R.layout.fragment_note_list) {

    private lateinit var binding: FragmentNoteListBinding
    private val viewModel by sharedViewModel<NoteViewModel>()
    private val noteListAdapter by lazy {
        NoteListAdapter { note, isDelete ->
            if (isDelete) {
                deleteNote(note)
            } else {
                val action = NoteListFragmentDirections.actionNoteListFragmentToNoteEditFragment(note)
                view?.let { Navigation.findNavController(it).navigate(action) }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNoteListBinding.bind(view)
        binding.noteList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = noteListAdapter
        }

        binding.appBar.newNoteOrSaveText.setText(R.string.new_note_text)
        binding.appBar.noteActionContainer.setOnClickListener {
            val action = NoteListFragmentDirections.actionNoteListFragmentToNoteEditFragment()
            Navigation.findNavController(it).navigate(action)
        }

        viewModel.getAllNotes()
        observeData()
    }

    private fun observeData() {
        viewModel.noteList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.warningText.isVisible = true
                binding.noteList.isVisible = false
            } else {
                binding.noteList.isVisible = true
                binding.warningText.isVisible = false
                noteListAdapter.submitList(it.reversed())
                binding.noteList.postDelayed({
                    if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                        binding.noteList.smoothScrollToPosition(0)
                    }
                }, 500)
            }
        }
    }

    private fun deleteNote(note: Note) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.alert_title)
        builder.setMessage(R.string.alert_message)
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            viewModel.deleteNote(note)
        }
        builder.setNegativeButton(android.R.string.no) { _, _ -> }
        builder.show()
    }

}