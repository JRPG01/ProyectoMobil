package com.example.notas.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditViewModel(): ViewModel(){
    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()
    var currentTitulo by mutableStateOf("")
        private set

    var currentNota by mutableStateOf("")
        private set

    fun updateUserGuess(search: String){
        currentTitulo = search
    }

    fun updateNote(text: String){
        currentNota = text
    }
}