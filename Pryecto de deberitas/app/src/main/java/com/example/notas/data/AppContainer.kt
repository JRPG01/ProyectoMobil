package com.example.notas.data

import android.content.Context

interface AppContainer {
    val notasRepository: NotasRepository
    val tareasRepository: TareasRepository
}
class AppDataContainer(private val context: Context) : AppContainer {

    override val notasRepository: NotasRepository by lazy {
        OfflineNotasRepository(NotasDatabase.getDatabase(context).NotaDao())
    }
    override val tareasRepository: TareasRepository by lazy {
        OfflinesTareasRepository(NotasDatabase.getDatabase(context).TareasDao())
    }
}