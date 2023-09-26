package com.example.notesandtasks.DataBase

import android.icu.text.CaseMap.Title
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.notesandtasks.Modelos.Notes

@Dao
interface MainDAO {
    @Insert(onConflict = REPLACE)
    fun insert(notes: Notes) {
    }
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAll(): List<Notes>

    @Query("UPDATE notes SET Titulo = :title, notas = :notes where ID= :id")
    fun update(id:Int, title:String, notes:String) {

    }
    @Delete
    fun delet(notes: Notes) {

    }
}