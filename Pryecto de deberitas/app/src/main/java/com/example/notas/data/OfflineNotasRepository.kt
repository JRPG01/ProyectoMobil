package com.example.notas.data

import kotlinx.coroutines.flow.Flow

class OfflineNotasRepository(private val notasDao: NotaDao):NotasRepository{
    override fun getAllNotasStream(): Flow<List<Notas>> = notasDao.getAllNotas()

    override fun getNotasStream(id: Int): Flow<Notas?> = notasDao.getNotas(id)

    override suspend fun insertNotas(notas: Notas) = notasDao.insert(notas)

    override suspend fun deleteNotas(notas: Notas) = notasDao.delete(notas)

    override suspend fun updateNotas(notas: Notas) = notasDao.update(notas)
}