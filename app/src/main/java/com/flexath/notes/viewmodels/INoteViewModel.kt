package com.flexath.notes.viewmodels

import androidx.lifecycle.LiveData
import com.flexath.notes.data.model.NoteEntity
import kotlinx.coroutines.Job

interface INoteViewModel {
    fun insertNote(note: NoteEntity): Job
    fun deleteNote(note: NoteEntity): Job
    fun updateNote(note: NoteEntity): Job
    fun getAllNotes(): LiveData<List<NoteEntity>>?
}