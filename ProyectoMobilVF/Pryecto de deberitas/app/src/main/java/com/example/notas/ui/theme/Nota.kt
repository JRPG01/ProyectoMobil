package com.example.notas.ui.theme

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositionLocalContext
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
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.notas.Navegacion.Screean
import java.util.Calendar
import java.util.Date
import coil.compose.AsyncImage
import com.example.notas.data.Notas
import com.example.notas.data.NotasDatabase
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.launch
import java.io.File


class Nota : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
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

@RequiresApi(Build.VERSION_CODES.S)
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

    val contestonotificaiones = LocalContext.current
    val idCanal = "Tareas"
    val idNotificacionTareas = 0

    LaunchedEffect(Unit){
        crearCanalNotificacion(idCanal,contestonotificaiones)

    }

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
                            notificacionBasica(
                                contestonotificaiones,
                                idCanal,
                                idNotificacionTareas,
                                "Notificacion de tarea",
                                "La fecha limite de la tarea finalizado"
                            )
                            notificaionProgramada(contestonotificaiones)
                        //mDatePickerDialog.show()
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

fun notificacionBasica(
    contestonotificaiones: Context,
    idCanal: String,
    idNotificacionTareas: Int,
    s: String,
    s1: String,
    prioriad: Int = NotificationCompat.PRIORITY_DEFAULT
) {
    val builder = NotificationCompat.Builder(contestonotificaiones,idCanal)
        .setSmallIcon(androidx.media.R.drawable.notification_bg)
        .setContentTitle(s)
        .setContentText(s1)
        .setPriority(prioriad)

    with(NotificationManagerCompat.from(contestonotificaiones)){
        if (ActivityCompat.checkSelfPermission(
                contestonotificaiones,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notify(idNotificacionTareas,builder.build())
    }
}



@SuppressLint("ScheduleExactAlarm")
@RequiresApi(Build.VERSION_CODES.S)
fun notificaionProgramada(mContext: Context) {
    val intent = Intent(mContext,notificacionProgramada::class.java)
    val penddingIntent = PendingIntent.getBroadcast(
        mContext,
        notificacionProgramada.NOTIFICACION_ID,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    var alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        Calendar.getInstance().timeInMillis+10000,penddingIntent
    )
}


@RequiresApi(Build.VERSION_CODES.S)
@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldNotas(navController: NavController,
                  noteViewModel: NotesViewModel= viewModel()) {

    val noteUiState by noteViewModel.uiState.collectAsState()
    //variable
    val c = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    //variables
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
                    Spacer(modifier = Modifier.size(300.dp))
                    Button(onClick = {
                        //insercion
                        scope.launch {
                            val db = Room.databaseBuilder(c, NotasDatabase::class.java, "notas_database")
                                    .build()
                            val g = Notas(0, noteViewModel.currentSearch, noteViewModel.currentNote, "",)
                            db.NotaDao().insert(g)
                        }
                        //insercion
                    }) {
                        Icon(Icons.Default.Save , contentDescription = "Save note")
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
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun canvas(noteChange: (String) -> Unit,
           currentNote:String,
           modifier: Modifier = Modifier) {

     Scaffold(){innerPadding->

         // VARIABLES DE MULTIMEDIA
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
         var  listaUri = remember {
             mutableListOf<Uri?>()
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
         val context = LocalContext.current

         Column(modifier = Modifier
             .padding(innerPadding),
             verticalArrangement = Arrangement.spacedBy(16.dp),) {
             TextField(value = currentNote,
                 onValueChange =noteChange,
                 modifier = modifier
                     .width(380.dp)
                     .height(150.dp))
             Spacer(modifier = Modifier.size(20.dp))
             Row (Modifier.horizontalScroll(rememberScrollState())){
                 if ((hasImage or hasVideo) && imageUri != null) {
                     listaUri.add(imageUri)
                     // 5
                     for (imagen in listaUri) {
                         if (hasImage) {
                             AsyncImage(
                                 model = imagen,
                                 modifier = Modifier
                                     .height(100.dp)
                                     .width(100.dp),
                                 contentDescription = "Selected image",
                             )
                         }
                     }
                     if(hasVideo) {VideoPlayer(videoUri = imageUri!!)}
                 }
             }
             Spacer(modifier = Modifier.size(100.dp))
             Row {
                 Spacer(modifier = Modifier.size(60.dp))
                 Button(onClick = {
                     uri = ComposeFileProvider.getImageUri(context)
                     cameraLauncher.launch(uri)}
                 ) {
                     Icon(imageVector = Icons.Default.Camera, contentDescription = "Camera Button")
                 }
                 Spacer(modifier = Modifier.size(20.dp))
                 Button(onClick = {imagePicker.launch("image/*")}) {
                     Icon(imageVector = Icons.Default.Image, contentDescription = "Galery Button")
                 }
                 Spacer(modifier = Modifier.size(20.dp))
                 Button(
                     onClick = {
                         val uri = ComposeFileProvider.getImageUri(context)
                         videoLauncher.launch(uri)
                         imageUri = uri
                     },
                 ) {
                     Icon(imageVector = Icons.Default.Videocam, contentDescription = "Vidio Button")
                 }
                 Spacer(modifier = Modifier.size(20.dp))
             }
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

// -----------------------------------------------------------------NOTIFICACION---------------------------------------------------------------

fun crearCanalNotificacion(idCanal: String, contestonotificaiones: Context) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val nombre = "CanalTareas"
        val descripcion = "Canal de notificaciones de tareas"
        val importancia = NotificationManager.IMPORTANCE_DEFAULT
        val canal = NotificationChannel(idCanal,nombre,importancia)
            .apply {
                description = descripcion
            }
        val notificationManager: NotificationManager =
            contestonotificaiones.getSystemService(Context.NOTIFICATION_SERVICE) as
                    NotificationManager
        notificationManager.createNotificationChannel(canal)
    }
}
