package com.example.notas.data

import kotlinx.coroutines.flow.Flow

class OfflinesTareasRepository(private val tareasDao: TareasDao):TareasRepository {
    override fun getAllTareaStream(): Flow<List<Tarea>> = tareasDao.getAllTareas()

    override fun getTareaStream(): Flow<Tarea?> = tareasDao.getTareas()

    override suspend fun insertTarea(tarea: Tarea) = tareasDao.insert(tarea)

    override suspend fun deleteTarea(tarea: Tarea) = tareasDao.delete(tarea)

    override suspend fun updateTarea(tarea: Tarea) = tareasDao.update(tarea)
}