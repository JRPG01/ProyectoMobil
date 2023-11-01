package com.example.notas.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [Notas::class,Tarea::class], version = 1, exportSchema = false)
abstract class NotasDatabase:RoomDatabase(){
    abstract fun NotaDao():NotaDao
    abstract fun TareasDao():TareasDao
    companion object{
        @Volatile
        private var Instance: NotasDatabase? = null
        fun getDatabase(context: Context): NotasDatabase{
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context,NotasDatabase::class.java, "notas_database").fallbackToDestructiveMigration().build().also { Instance=it }
            }
        }
    }

}