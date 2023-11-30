package com.example.notas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.notas.Navegacion.Navegation
import com.example.notas.ui.theme.AndroidAudioPlayer
import com.example.notas.ui.theme.AndroidAudioRecorder
import com.example.notas.ui.theme.GrabarAudioScreen
import com.example.notas.ui.theme.NotasTheme
import java.io.File

class MainActivity : ComponentActivity() {
    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    private var audioFile: File? = null
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotasTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navegation(navHostController = rememberNavController())
                    GrabarAudioScreen(
                        onClickStGra = {
                            File(cacheDir, "audio.mp3").also {
                                recorder.start(it)
                                audioFile = it
                            }

                        },
                        onClickSpGra = {recorder.stop()},
                        onClickStRe = { audioFile?.let { player.start(it) } },
                        onClickSpRe = {player.stop()}
                    )
                }
            }
        }
    }
}

