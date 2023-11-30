package com.example.notas.ui.theme

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.notas.Navegacion.Screean
import java.util.Calendar
import java.util.Date
import coil.compose.AsyncImage
import com.example.notas.alarmas.AlarmSchedulerImpl
import com.example.notas.alarmas.AlarmasScreen
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import java.io.File


class Nota : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotasTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScaffoldNotas(navController = rememberNavController())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppNotas(naveController: NavController,
    modifier: Modifier = Modifier) {
    var name by remember {
        mutableStateOf("")
    }
    /*calendario*/
    // Fetching the Local Context
    val mContext = LocalContext.current

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring a string value to
    // store date in string format
    val mDate = remember { mutableStateOf("") }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
        }, mYear, mMonth, mDay
    )

    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {naveController.navigate(Screean.NotasPrincipalScreean.route)}) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Regresa al menu"
                    )
                }
                TextField(
                    value = name,
                    onValueChange = {name = it},
                    label = { Text(text = "Titulo")},
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick ={
                        mDatePickerDialog.show()
                        }, colors = IconButtonDefaults.iconButtonColors(Color(0,0,0, 0)) ){
                            Icon(
                                imageVector = Icons.Outlined.DateRange,
                                contentDescription = "Fecha de tarea"
                            )
                        }
                    }
                )

            }
        },
        modifier = modifier.fillMaxWidth()
    )
}


@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldNotas(navController: NavController,
                  noteViewModel: NotesViewModel= viewModel()) {

    val noteUiState by noteViewModel.uiState.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppNotas(naveController = navController)
        },
        bottomBar = {

            BottomAppBar(

                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Row {
                    Button(onClick = {showBottomSheet = true}) {
                        Icon(Icons.Default.Add , contentDescription = "Add Imagen")
                        Text(text = "Img")
                    }
                    Spacer(modifier = Modifier.size(22.dp))
                    Button(onClick = {}) {
                        Icon(Icons.Default.Done , contentDescription = "Save note")
                        Text(text = "Guardar")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            canvas(
                noteChange = {noteViewModel.updateNote(it)},
                currentNote = noteViewModel.currentNote,
            )
        }

        // Screen content
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {

                var uri : Uri? = null
                // 1
                var hasImage by remember {
                    mutableStateOf(false)
                }
                var hasVideo by remember {
                    mutableStateOf(false)
                }
                // 2
                var imageUri by remember {
                    mutableStateOf<Uri?>(null)
                }


                val imagePicker = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = { uri ->
                        // TODO
                        // 3
                        hasImage = uri != null
                        imageUri = uri
                    }
                )

                val cameraLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.TakePicture(),
                    onResult = { success ->
                        Log.d("IMG", hasImage.toString())
                        Log.d("URI", imageUri.toString())
                        if(success) imageUri = uri
                        hasImage = success
                    }
                )

                val videoLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.CaptureVideo(),
                    onResult = { success ->
                        hasVideo = success
                    }
                )

                Box{}
                val context = LocalContext.current

                if ((hasImage or hasVideo) && imageUri != null) {
                    // 5
                    if(hasImage){
                        AsyncImage(
                            model = imageUri,
                            modifier = Modifier.fillMaxWidth(),
                            contentDescription = "Selected image",
                        )
                    }
                    if(hasVideo) {VideoPlayer(videoUri = imageUri!!)}
                }

                // Experimental
                //val uri = ComposeFileProvider.getImageUri(applicationContext)
                // FIn de lo Experimental

                Row {
                    Spacer(modifier = Modifier.size(60.dp))
                    Button(onClick = {
                        uri = ComposeFileProvider.getImageUri(context)
                        cameraLauncher.launch(uri)}
                    ) {
                        Icon(imageVector = Icons.Default.Camera, contentDescription = "Camera Button")
                    }
                    Spacer(modifier = Modifier.size(30.dp))
                    Button(onClick = {imagePicker.launch("image/*")}) {
                        Icon(imageVector = Icons.Default.Image, contentDescription = "Galery Button")
                    }
                    Spacer(modifier = Modifier.size(30.dp))
                    Button(
                        onClick = {
                            val uri = ComposeFileProvider.getImageUri(context)
                            videoLauncher.launch(uri)
                            imageUri = uri
                        },
                    ) {
                        Icon(imageVector = Icons.Default.Videocam, contentDescription = "Vidio Button")
                    }
                    Spacer(modifier = Modifier.size(30.dp))
                }
                // Sheet content
                Spacer(modifier = Modifier.size(50.dp))
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun canvas(noteChange: (String) -> Unit,
           currentNote:String,
           modifier: Modifier = Modifier) {

     Scaffold(){innerPadding->

         Column(modifier = Modifier
             .padding(innerPadding),
             verticalArrangement = Arrangement.spacedBy(16.dp),) {
             TextField(value = currentNote,
                 onValueChange =noteChange,
                 modifier = modifier.fillMaxSize())
         }
     }
}


// Tomar Fotos
@Composable
fun ImagePicker(
    modifier: Modifier = Modifier,
) {
}


@Composable
fun VideoPlayer(videoUri: Uri, modifier: Modifier = Modifier.fillMaxWidth()) {
    val context = LocalContext.current
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
        }
    }
    val playbackState = exoPlayer
    val isPlaying = playbackState?.isPlaying ?: false

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
            }
        },
        modifier = modifier
    )

    IconButton(
        onClick = {
            if (isPlaying) {
                exoPlayer.pause()
            } else {
                exoPlayer.play()
            }
        },
        modifier = Modifier
            //.align(Alignment.BottomEnd)
            .padding(16.dp)
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Filled.Refresh else Icons.Filled.PlayArrow,
            contentDescription = if (isPlaying) "Pause" else "Play",
            tint = Color.White,
            modifier = Modifier.size(48.dp)
        )
    }
}
