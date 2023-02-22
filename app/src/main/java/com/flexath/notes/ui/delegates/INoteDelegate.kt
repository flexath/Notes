package com.flexath.notes.ui.delegates

import com.flexath.notes.data.model.NoteEntity

interface INoteDelegate {
    fun onClickNote(note: NoteEntity)
    fun onClickDeleteButton(note:NoteEntity)
    fun onLongClickNote(note:NoteEntity)
}