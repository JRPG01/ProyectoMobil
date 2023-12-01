package com.example.notas

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notas.Navegacion.Screean
import com.example.notas.app.notesApp
import com.example.notas.ui.theme.EditViewModel
import com.example.notas.ui.theme.NotesViewModel
import com.example.notas.ui.theme.PrincipalScreanViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            NotesViewModel(notesApp().container.notasRepository)
        }

        initializer {
            PrincipalScreanViewModel(notesApp().container.notasRepository)
        }

        initializer {
            EditViewModel(this.createSavedStateHandle(), notesApp().container.notasRepository)
        }

    }
}

fun CreationExtras.NotesApplication(): notesApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as notesApp)