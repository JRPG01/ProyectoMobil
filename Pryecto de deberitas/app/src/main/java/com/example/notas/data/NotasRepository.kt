package com.example.notas.data

import kotlinx.coroutines.flow.Flow

interface NotasRepository {
    fun getAllNotasStream(): Flow<List<Notas>>
    fun getNotasStream(id: Int): Flow<Notas?>
    suspend fun insertNotas(notas: Notas)
    suspend fun deleteNotas(notas: Notas)
    suspend fun updateNotas(notas: Notas)
}