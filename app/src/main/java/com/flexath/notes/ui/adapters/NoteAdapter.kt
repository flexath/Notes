package com.flexath.notes.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flexath.notes.R
import com.flexath.notes.data.model.NoteEntity
import com.flexath.notes.ui.delegates.INoteDelegate
import com.flexath.notes.ui.viewholders.NoteViewHolder
import com.flexath.notes.viewmodels.INoteViewModel
import kotlinx.android.synthetic.main.view_holder_note_list.view.*
import java.util.ArrayList

class NoteAdapter(
    private val delegate: INoteDelegate,
    private var noteList: List<NoteEntity>
) : RecyclerView.Adapter<NoteViewHolder>() {

    var noteEntity:NoteEntity? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_note_list, parent, false)
        return NoteViewHolder(view, delegate)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.bindNoteData(noteList[position])
        noteEntity = note
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSearchNoteList(noteListSearch: ArrayList<NoteEntity>) {
        noteList = noteListSearch
        notifyDataSetChanged()
    }

    fun getNoteAt(position: Int): NoteEntity {
        return noteList[position]
    }
}