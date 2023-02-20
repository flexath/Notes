package com.flexath.notes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flexath.notes.R
import com.flexath.notes.data.model.NoteEntity
import com.flexath.notes.ui.delegates.INoteDelegate
import com.flexath.notes.ui.viewholders.NoteViewHolder
import com.flexath.notes.viewmodels.INoteViewModel
import kotlinx.android.synthetic.main.view_holder_note_list.view.*

class NoteAdapter(
    private val delegate: INoteDelegate,
    private val noteList: List<NoteEntity>,
    private val viewModel: INoteViewModel
) : RecyclerView.Adapter<NoteViewHolder>() {

    lateinit var noteEntity:NoteEntity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_note_list, parent, false)
        return NoteViewHolder(view, delegate)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.bindNoteData(noteList[position])
        noteEntity = note

        holder.itemView.btnDeleteNoteHome.setOnClickListener {
            delegate.onClickDeleteButton(note)
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun deleteNoteDialog(note:NoteEntity) {
        viewModel.deleteNote(note)
    }

    fun getNoteEntity() {

    }
}