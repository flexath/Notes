package com.flexath.notes.data.repository

import androidx.lifecycle.LiveData
import com.flexath.notes.data.model.NoteDao
import com.flexath.notes.data.model.NoteEntity

class RoomNoteRepository(private val dao:NoteDao) : INoteRepository {
    override suspend fun insertNote(note: NoteEntity) = dao.insertNote(note)
    override suspend fun deleteNote(note: NoteEntity) = dao.deleteNote(note)
    override suspend fun updateNote(note: NoteEntity) = dao.updateNote(note)
    override fun getAllNotes(): LiveData<List<NoteEntity>> = dao.getAllNotes()
}