package com.flexath.notes.data.repository

import androidx.lifecycle.LiveData
import com.flexath.notes.data.model.NoteEntity

interface INoteRepository {
    suspend fun insertNote(note:NoteEntity)
    suspend fun deleteNote(note:NoteEntity)
    suspend fun updateNote(note:NoteEntity)
    fun getAllNotes():LiveData<List<NoteEntity>>?
}