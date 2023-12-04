package com.example.notas.ui.theme

import android.Manifest
import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.notas.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.io.File
import java.io.FileOutputStream


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GrabarAudioScreen( onClickStGra: () -> Unit,
                       onClickSpGra: () -> Unit,
                       onClickStRe: () -> Unit,
                       onClickSpRe: () -> Unit,){
    val context = LocalContext.current

    val recordAudioPermissionState = rememberPermissionState(
        Manifest.permission.RECORD_AUDIO
    )

    var rationaleState by remember {
        mutableStateOf<RationaleState?>(null)
    }

    Box(
        Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Show rationale dialog when needed
            rationaleState?.run { PermissionRationaleDialog(rationaleState = this) }

            PermissionRequestButton(
                isGranted = recordAudioPermissionState.status.isGranted,
                title = stringResource(R.string.record_audio),
                onClickStGra,
                onClickSpGra,
                onClickStRe,
                onClickSpRe
            ) {
                if (recordAudioPermissionState.status.shouldShowRationale) {
                    rationaleState = RationaleState(
                        "Permiso para grabar audio",
                        "In order to use this feature please grant access by accepting " + "the grabar audio dialog." + "\n\nWould you like to continue?",
                    ) { proceed ->
                        if (proceed) {
                            recordAudioPermissionState.launchPermissionRequest()
                        }
                        rationaleState = null
                    }
                } else {
                    recordAudioPermissionState.launchPermissionRequest()
                }
            }

        }
    }

}

@Composable
fun PermissionRequestButton(isGranted: Boolean, title: String,
                            onClickStGra: () -> Unit,
                            onClickSpGra: () -> Unit,
                            onClickStRe: () -> Unit,
                            onClickSpRe: () -> Unit,
                            onClick: () -> Unit) {
    if (isGranted) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Spacer(Modifier.size(10.dp))
            Text(text = title, modifier = Modifier.background(Color.Transparent))
            //Spacer(Modifier.size(10.dp))

        }
        //Spacer(modifier = Modifier.size(595.dp))
        Row {
            Button(onClick = onClickStGra) {
                Icon(imageVector = Icons.Default.AudioFile , contentDescription = "Iniciar Audio" )
            }
            Button(onClick = onClickSpGra) {
                Icon(imageVector = Icons.Default.Stop , contentDescription = "Iniciar Audio" )
            }
            Button(onClick = onClickStRe) {
                Icon(imageVector = Icons.Default.PlayCircle , contentDescription = "Reproduce Audio" )
            }
            Button(onClick = onClickSpRe) {
                Icon(imageVector = Icons.Default.Pause , contentDescription = "Pause Audio" )
            }
        }
    } else {
        Button(onClick = onClick) {
            Text("Request $title")
        }
    }
}

/**
 * Simple AlertDialog that displays the given rational state
 * Cuadro de dialogo simple que muestra el estado del rational
 */
@Composable
fun PermissionRationaleDialog(rationaleState: RationaleState) {
    AlertDialog(onDismissRequest = { rationaleState.onRationaleReply(false) }, title = {
        Text(text = rationaleState.title)
    }, text = {
        Text(text = rationaleState.rationale)
    }, confirmButton = {
        TextButton(onClick = {
            rationaleState.onRationaleReply(true)
        }) {
            Text("Continue")
        }
    }, dismissButton = {
        TextButton(onClick = {
            rationaleState.onRationaleReply(false)
        }) {
            Text("Dismiss")
        }
    })
}
data class RationaleState(
    val title: String,
    val rationale: String,
    val onRationaleReply: (proceed: Boolean) -> Unit,
)

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}
class AndroidAudioRecorder(
    private val context: Context
): AudioRecorder {

    private var recorder: MediaRecorder? = null


    private fun createRecorder(): MediaRecorder {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    override fun start(outputFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)

            prepare()
            start()

            recorder = this
        }
    }

    override fun stop() {
        recorder?.stop()
        recorder?.reset()
        recorder = null
    }
}

class AndroidAudioPlayer(
    private val context: Context
): AudioRecorder {

    private var player: MediaPlayer? = null


    override fun start(outputFile: File) {
        MediaPlayer.create(context, outputFile.toUri()).apply {
            player = this
            start()
        }
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
}
