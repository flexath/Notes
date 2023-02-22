package com.flexath.notes.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.flexath.notes.data.model.NoteEntity
import com.flexath.notes.ui.delegates.INoteDelegate
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.view_holder_note_list.view.*

class NoteViewHolder(itemView: View,private val delegate:INoteDelegate) : RecyclerView.ViewHolder(itemView) {

    private lateinit var note:NoteEntity

    init {
        setUpListeners()
    }

    private fun setUpListeners() {
        itemView.setOnClickListener {
            delegate.onClickNote(note)
        }
        itemView.btnDeleteNoteHome.setOnClickListener {
            delegate.onClickDeleteButton(note)
        }
        itemView.setOnLongClickListener {
            delegate.onLongClickNote(note)
            true
        }
    }

    fun bindNoteData(note:NoteEntity) {
        this.note = note
        itemView.tvNoteTitleHome.text = note.title
        itemView.tvNoteDescriptionHome.text = note.description
        itemView.tvNoteDateHome.text = note.date
        itemView.background = itemView.resources.getDrawable(note.color)
    }
}