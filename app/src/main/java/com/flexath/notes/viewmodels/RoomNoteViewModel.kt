package com.flexath.notes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flexath.notes.data.model.NoteEntity
import com.flexath.notes.data.repository.INoteRepository
import kotlinx.coroutines.launch

class RoomNoteViewModel(private val repository:INoteRepository) : ViewModel(),INoteViewModel {

    override fun insertNote(note: NoteEntity) = viewModelScope.launch {
        repository.insertNote(note)
    }

    override fun deleteNote(note: NoteEntity) = viewModelScope.launch {
        repository.deleteNote(note)
    }

    override fun updateNote(note: NoteEntity) = viewModelScope.launch {
        repository.updateNote(note)
    }

    override fun getAllNotes(): LiveData<List<NoteEntity>>? = repository.getAllNotes()
}