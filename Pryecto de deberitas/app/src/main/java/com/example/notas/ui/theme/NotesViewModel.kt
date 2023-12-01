package com.example.notas.ui.theme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.notas.data.NotasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class NotesViewModel(notesRepository: NotasRepository): ViewModel() {
    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()
    var currentSearch by mutableStateOf("")
        private set

    var currentNote by mutableStateOf("")
        private set

    fun updateUserGuess(search: String){
         currentSearch = search
    }

    fun updateNote(text: String){
        currentNote = text
    }

}