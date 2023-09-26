package com.example.notesandtasks.Modelos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "notes")
@Serializable
public class Notes{
    @PrimaryKey(autoGenerate = true)
    var ID: Int = 0
        get() = field
        set(value){field = value}

    @ColumnInfo(name = "titulo")
    var Titulo: String=""
        get() = field
        set(value){field = value}

    @ColumnInfo(name = "notas")
    var notas: String = ""
        get() = field
        set(value){field = value}

    @ColumnInfo(name = "fecha")
    var fecha: String = ""
        get() = field
        set(value){field = value}

    @ColumnInfo(name = "pin")
    var pin:Boolean= false
        get() = field
        set(value){field = value}

}