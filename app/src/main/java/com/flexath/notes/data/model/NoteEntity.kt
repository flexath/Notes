package com.flexath.notes.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "note_table")
data class NoteEntity(
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "color") val color: Int
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}