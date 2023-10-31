package com.example.notas.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notas: Notas)
    @Update
    suspend fun update(notas: Notas)
    @Delete
    suspend fun delete(notas: Notas)
    @Query("SELECT * from notas WHERE id = id")
    fun getItem(id: Int): Flow<Notas>
    @Query("SELECT * from notas ORDER BY Titulo ASC")
    fun getAllItems(): Flow<List<Notas>>
}