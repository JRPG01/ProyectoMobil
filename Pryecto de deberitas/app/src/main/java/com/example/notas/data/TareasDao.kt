package com.example.notas.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Dao
interface TareasDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tarea: Tarea)
    @Update
    suspend fun update(tarea: Tarea)
    @Delete
    suspend fun delete(tareas: Tarea)
    @Query("SELECT * from notas WHERE id = id")
    fun getTareas(id: Int): Flow<Tarea>
    @Query("SELECT * from tareas ORDER BY Titulo ASC")
    fun getAllTareas(): Flow<List<Tarea>>
}