package com.example.notas.data

import kotlinx.coroutines.flow.Flow

interface TareasRepository {
    fun getAllTareaStream(): Flow<List<Tarea>>
    fun getTareaStream(id: Int): Flow<Tarea?>
    suspend fun insertTarea(tarea: Tarea)
    suspend fun deleteTarea(tarea: Tarea)
    suspend fun updateTarea(tarea: Tarea)
}