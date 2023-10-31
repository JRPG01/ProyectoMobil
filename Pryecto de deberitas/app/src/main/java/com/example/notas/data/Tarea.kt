package com.example.notas.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob
import java.sql.Date

@Entity(tableName = "tareas")
data class Tarea(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "Titulo") val title: String,
    @ColumnInfo(name = "Cuerpo") val body: String,
    @ColumnInfo(name = "Imagen") val Imag: Blob,
    @ColumnInfo(name = "Video") val Vid: Blob,
    @ColumnInfo(name = "Fecha") val data: Date
)