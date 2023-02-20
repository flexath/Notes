package com.flexath.notes.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao:NoteDao

    companion object {
        @Volatile
        private var databaseInstance:NoteDatabase? = null

        fun getInstance(context: Context) = databaseInstance ?: synchronized(this){
            databaseInstance ?: Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "NoteDatabase"
            ).build().also {
                databaseInstance = it
            }
        }
    }
}