package com.task.noteapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.daimajia.swipe.SwipeLayout
import com.task.noteapp.R
import com.task.noteapp.data.local.entity.Note
import com.task.noteapp.databinding.NoteItemBinding

internal class NoteListAdapter(private val onNoteClick: (note: Note, isDelete: Boolean) -> Unit) :
    ListAdapter<Note, NoteListAdapter.NoteViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NoteViewHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val brokeImage = AppCompatResources.getDrawable(binding.noteImage.context, R.drawable.broken_image)

        init {
            binding.noteView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onNoteClick.invoke(getItem(adapterPosition), false)
                }
            }
            binding.itemNoteSwipeContainer.showMode = SwipeLayout.ShowMode.LayDown
            binding.itemNoteSwipeContainer.close()

            binding.delete.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    binding.itemNoteSwipeContainer.close()
                    onNoteClick.invoke(getItem(adapterPosition), true)
                }
            }
        }

        fun bind(note: Note) {
            binding.noteTitle.text = note.title
            binding.noteDescription.text = note.description
            binding.createdOrEditedDate.text = itemView.context.getString(R.string.created_text, note.createdDate)
            binding.noteDescription.isVisible = !note.description.isNullOrEmpty()
            if (note.imageUrl.isNullOrEmpty()) {
                binding.noteImage.isVisible = false
            } else {
                binding.noteImage.isVisible = true
                Glide.with(binding.noteImage)
                    .load(note.imageUrl)
                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(12)))
                    .placeholder(brokeImage)
                    .error(brokeImage)
                    .into(binding.noteImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }


}