package com.example.notesandtasks.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notesandtasks.Modelos.Notes

@Database(entities = [Notes::class], version = 1, exportSchema = false)
abstract class RoomDB: RoomDatabase() {
    abstract fun MainDAO(): MainDAO

}