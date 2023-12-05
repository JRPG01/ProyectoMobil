package com.example.appnotas

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.FileOpen
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.appnotas.Multimedia.AudioPlayer
import com.example.appnotas.Multimedia.ComposeFileProvider
import com.example.appnotas.Multimedia.DialogShowAudioSelected
import com.example.appnotas.Multimedia.DialogShowFileSelected
import com.example.appnotas.Multimedia.DialogShowImageTake
import com.example.appnotas.Multimedia.DialogShowVideoTake
import com.example.appnotas.Multimedia.VideoPlayer
import com.example.appnotas.Multimedia.saveImageToGallery
import com.example.appnotas.Multimedia.saveVideoToGallery
import com.example.appnotas.Notifications.createChannelNotification
import com.example.appnotas.Notifications.workAlarm
import com.example.appnotas.Room.WorksData
import com.example.appnotas.ViewModel.NotasViewModel
import com.example.appnotas.utils.MultiNavigationType
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TareaScreen(multiViewModel: NotasViewModel, navigationType: MultiNavigationType) {
    var preview by remember {
        mutableStateOf(
            WorksData(-1,"Preview Tarea","Esta es la descripcion genera de todo.", LocalDate.now(), LocalTime.now(), emptyList(), emptyList(), emptyList(), emptyList())
        )
    }

    val multiUiState by multiViewModel.uiState.collectAsState()

    val tareasItems = multiUiState.works

    Row {
        Column(
            Modifier.weight(1f)
        ) {
            TopBar()

            Box {
                if(multiUiState.countHomeworks > 0){
                    // tareas sin caducar
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ){
                        // lista de tareas pendientes
                        item {
                            Text(
                                text = "Tareas pendientes",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                        items(tareasItems.size){ index ->
                            val item = tareasItems[index]
                            if(item.datework >= LocalDate.now()){
                                TareaBody(item,multiViewModel,navigationType){
                                    preview = item
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.size(16.dp))
                            Text(
                                text = "Tareas finalizadas",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                        // lista de tareas caducadas o finalizadas
                        items(tareasItems.size){ index ->
                            val item = tareasItems[index]
                            if(item.datework < LocalDate.now()){
                                TareaBody(item,multiViewModel,navigationType){
                                    preview = item
                                }
                            }
                        }
                    }

                } else {
                    // tip de tareas
                    Column(
                        Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.tip_homeworks),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                        )
                    }
                }

                FABody(multiViewModel,navigationType)
            }
        }

        if(navigationType == MultiNavigationType.NAVIGATION_RAIL){
            MultiDetailsScreenWorks(
                preview,
                multiViewModel,
                navigationType
            ){
                item -> preview=item
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(text = stringResource(id = R.string.tareas_title))
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun FABody(
    multiViewModel: NotasViewModel,
    navigationType: MultiNavigationType
) {
    var tarea by remember {
        mutableStateOf(false)
    }

    if(tarea){
        DialogAddTarea(
            multiViewModel = multiViewModel,
            onClick = {
                tarea = !tarea
            },
            navigationType = navigationType
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = {
                tarea = !tarea
            },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TareaBody(
    item: WorksData,
    multiViewModel: NotasViewModel,
    navigationType: MultiNavigationType,
    changePreview: (WorksData) -> Unit
) {
    val msg = buildAnnotatedString {
        append(stringResource(id = R.string.lblConfirmarP1_notas) + " ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(item.titlework)
        }
        append(" " + stringResource(id = R.string.lblConfirmarP2_notas))
    }

    var eliminar by remember {
        mutableStateOf(false)
    }

    var openDialog by remember {
        mutableStateOf(false)
    }

    var showMultimedia by remember {
        mutableStateOf(false)
    }

    if(openDialog){
        DialogAddTarea(
            onClick = { openDialog = !openDialog },
            multiViewModel = multiViewModel,
            update = true,
            tarea = item,
            navigationType = navigationType
        )
    }

    if(eliminar){
        DialogDeleteWork(
            onDismiss = { eliminar = !eliminar },
            item = item,
            msg = msg,
            multiViewModel = multiViewModel
        ) { item ->
            changePreview(item)
        }
    }

    if(showMultimedia){
        DialogShowMultimediaWork(
            onDismiss = { showMultimedia = !showMultimedia },
            item = item
        )
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .combinedClickable(
                onClick = {
                    openDialog = !openDialog
                },
                onLongClick = {
                    eliminar = !eliminar
                },
                onDoubleClick = {
                    showMultimedia = !showMultimedia
                }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(75.dp)
                .padding(PaddingValues(16.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // nombre y descripcion
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
            ) {
                Text(
                    text = item.titlework,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.size(16.dp))

                Text(
                    text = item.descwork,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if(eliminar){
                Checkbox(
                    checked = eliminar,
                    onCheckedChange = { eliminar = !eliminar }
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = item.datework.format(DateTimeFormatter.ofPattern("dd-MMM")),
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = item.hour.format(DateTimeFormatter.ofPattern("HH:mm")),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun DialogShowMultimediaWork(
    onDismiss: () -> Unit,
    item: WorksData
) {
    val context = LocalContext.current
    var uri: Uri by remember { mutableStateOf(Uri.EMPTY) }
    var showVideo by remember {
        mutableStateOf(false)
    }

    var showImage by remember {
        mutableStateOf(false)
    }

    var showAudio by remember {
        mutableStateOf(false)
    }

    if(showImage){
        Dialog(
            onDismissRequest = { showImage = !showImage },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(0.6f)
            )
        }
    }

    if(showVideo){
        Dialog(
            onDismissRequest = { showVideo = !showVideo },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            VideoPlayer(
                videoUri = uri,
                modifier = Modifier
                    .fillMaxSize(0.6f)
            )
        }
    }

    if(showAudio){
        Dialog(onDismissRequest = { showAudio = !showAudio }) {
            Card {
                AudioPlayer(audioUri = uri)
            }
        }
    }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card {
            Column(
                Modifier
                    .padding(PaddingValues(16.dp))
                    .fillMaxWidth(0.8f)
            ) {
                // imagenes
                Text(
                    text = "Imagenes",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    item {
                        item.images.forEach { item ->
                            AsyncImage(
                                model = item,
                                contentDescription = null,
                                modifier = Modifier
                                    .height(100.dp)
                                    .clickable {
                                        uri = item
                                        showImage = !showImage
                                    }
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                        }
                    }
                }

                // videos
                Text(
                    text = "Videos",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    item {
                        item.videos.forEach { item ->
                            Column(
                                Modifier
                                    .height(100.dp)
                                    .width(56.dp)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable {
                                        uri = item
                                        showVideo = !showVideo
                                    },
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) { }
                            Spacer(modifier = Modifier.size(8.dp))
                        }
                    }
                }

                // audios
                Text(
                    text = "Audios",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    item {
                        item.audios.forEach { item ->
                            Column(
                                Modifier
                                    .height(100.dp)
                                    .width(56.dp)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable {
                                        uri = item
                                        showAudio = !showAudio
                                    },
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) { }
                            Spacer(modifier = Modifier.size(8.dp))
                        }
                    }
                }

                // files
                Text(
                    text = "PDF",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    item {
                        item.audios.forEach { item ->
                            Column(
                                Modifier
                                    .height(100.dp)
                                    .width(56.dp)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable {
                                        uri = item
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            setDataAndType(uri, "application/pdf") // Cambia el tipo de MIME según el tipo de documento que estás mostrando
                                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        }
                                        context.startActivity(intent)
                                    },
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) { }
                            Spacer(modifier = Modifier.size(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DialogDeleteWork(
    multiViewModel: NotasViewModel,
    msg: AnnotatedString,
    onDismiss: () -> Unit,
    item: WorksData,
    changePreview: (WorksData) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Card(
            Modifier.fillMaxWidth(0.85f)
        ) {
            Column(
                Modifier.padding(PaddingValues(16.dp))
            ) {
                Text(
                    text = msg,
                    Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))
                // boton eliminar
                Button(
                    onClick = {
                        // eliminar
                        multiViewModel.deleteWork(item)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = stringResource(id = R.string.btnEliminar))
                }


                // boton cancelar
                TextButton(
                    onClick = {
                        onDismiss()
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = stringResource(id = R.string.btnCancelar))
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogAddTarea(
    onClick: () -> Unit,
    multiViewModel: NotasViewModel,
    update: Boolean = false,
    tarea: WorksData = WorksData(0,"","", LocalDate.now(), LocalTime.now(), emptyList(), emptyList(), emptyList(), emptyList()),
    navigationType: MultiNavigationType
) {
    // variables para en canal de notificacion
    val context = LocalContext.current
    val idCanal = "CanalTareas"

    // se crea el canal de notificacion en base a las variables anteriores
    LaunchedEffect(Unit){
        createChannelNotification(idCanal,context)
    }

    val timePickerState = rememberTimePickerState()

    var title by remember {
        mutableStateOf(if(update) tarea.titlework else "")
    }

    var desc by remember {
        mutableStateOf(if(update) tarea.descwork else "")
    }

    var hour by remember {
        mutableStateOf(false)
    }
    var hourSelected by remember {
        mutableStateOf(if(update) tarea.hour else LocalTime.now())
    }


    // calendario
    val calendar = Calendar.getInstance()

    // Fetching current year, month and day
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    var date: LocalDate by remember {
        mutableStateOf(if(update) tarea.datework else LocalDate.now())
    }

    val context1 = LocalContext.current
    val datePicker = DatePickerDialog(
        context1,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            date = LocalDate.of(selectedYear, selectedMonth+1, selectedDayOfMonth)
        }, year, month, dayOfMonth
    )


    if(hour){
        DialogSelectHour(
            onDismiss = {
                hour = !hour
            },
            state = timePickerState,
            selected = { hour ->
                hourSelected = hour
            }
        )
    }


    // multimedia
    var uri: Uri by remember { mutableStateOf(Uri.EMPTY) }

    // tomar foto INICIO ---------------------------------------------------------------------------

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var listImageUri by remember {
        mutableStateOf(listOf<Uri>())
    }
    var showImage by remember {
        mutableStateOf(false)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            uri.let {
                listImageUri = listImageUri + it // Agrega la Uri a la lista
            }
            imageUri = uri
            showImage = !showImage
        }
    }

    if(showImage){
        imageUri?.let {
            DialogShowImageTake(
                onDismiss = { showImage = !showImage },
                imageUri = it
            )
        }
    }
    // tomar foto FIN ------------------------------------------------------------------------------

    // tomar video INICIO --------------------------------------------------------------------------
    var videoUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var listVideoUri by remember {
        mutableStateOf(listOf<Uri>())
    }
    var showVideo by remember {
        mutableStateOf(false)
    }

    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        if (success) {
            uri.let {
                listVideoUri = listVideoUri + it // Agrega la Uri a la lista
            }
            videoUri = uri
            showVideo = !showVideo
        }
    }
    if(showVideo){
        DialogShowVideoTake(
            onDismiss = { showVideo = !showVideo },
            videoUri = videoUri
        )
    }
    // tomar video FIN -----------------------------------------------------------------------------


    // seleccionar audio INICIO ------------------------------------------------------------------
    var audioUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var listAudioUri by remember {
        mutableStateOf(listOf<Uri>())
    }
    var showAudio by remember {
        mutableStateOf(false)
    }

    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri?.let {
            // Aquí puedes manejar la URI del archivo seleccionado
            listAudioUri = listAudioUri + it // Agrega la Uri del archivo a la lista
        }
        audioUri = uri
        showAudio = !showAudio
    }

    if(showAudio){
        DialogShowAudioSelected(
            onDismiss = { showAudio = !showAudio },
            fileUri = audioUri
        )
    }
    // seleccionar audio FIN ---------------------------------------------------------------------

    // seleccionar audio INICIO ------------------------------------------------------------------
    var fileUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var listFileUri by remember {
        mutableStateOf(listOf<Uri>())
    }
    var showFile by remember {
        mutableStateOf(false)
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri?.let {
            // Aquí puedes manejar la URI del archivo seleccionado
            listFileUri = listFileUri + it // Agrega la Uri del archivo a la lista
        }
        fileUri = uri
        showFile = !showFile
    }

    if(showFile){
        DialogShowFileSelected(
            onDismiss = { showFile = !showFile }
        )
    }
    // seleccionar audio FIN ---------------------------------------------------------------------

    Dialog(
        onDismissRequest = { /*TODO*/ },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Spacer(modifier = Modifier.size(8.dp))

            // icono para cerrar el dialog
            Row(
                modifier = Modifier
                    .padding(PaddingValues(8.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Clear,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = { datePicker.show() },
                ) {
                    Text(
                        text = date.format(DateTimeFormatter.ofPattern("dd-MMM")),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                TextButton(
                    onClick = { hour = !hour },
                ) {
                    Text(
                        text = hourSelected.format(DateTimeFormatter.ofPattern("HH:mm")),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))
            }

            // caja de texto titlo de la nota
            Row(
                modifier = Modifier
                    .padding(PaddingValues(16.dp))
            ) {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.title_tareas),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    textStyle = MaterialTheme.typography.headlineSmall,
                    shape = RoundedCornerShape(0.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            // caja de texto de la descripcion de la nota

            Column(
                modifier = Modifier
                    .padding(PaddingValues(16.dp))
            ) {
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.title_tareas),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    textStyle = MaterialTheme.typography.titleMedium,
                    shape = RoundedCornerShape(0.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                )
            }

            // boton agregar nota
            // boton agregar voz, video y audio
            var documentos by remember {
                mutableStateOf(false)
            }

            Row(
                modifier = Modifier
                    .padding(
                        vertical = 0.dp,
                        horizontal = 16.dp
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                // archivos
                if(documentos){

                    // documentos
                    IconButton(
                        onClick = {
                            filePickerLauncher.launch("application/pdf")
                        },
                        modifier = Modifier
                            .width(75.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Outlined.FileOpen, contentDescription = null)
                            Text(
                                text = "PDF",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            audioPickerLauncher.launch("audio/*")
                        },
                        modifier = Modifier
                            .width(75.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Outlined.VolumeUp, contentDescription = null)
                            Text(
                                text = "Audio",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            uri = ComposeFileProvider.getImageUri(context)
                            videoLauncher.launch(uri)
                        },
                        modifier = Modifier
                            .width(75.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Outlined.VideoLibrary, contentDescription = null)
                            Text(
                                text = "Video",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            uri = ComposeFileProvider.getImageUri(context)
                            cameraLauncher.launch(uri)
                        },
                        modifier = Modifier
                            .width(75.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Outlined.AccountBox, contentDescription = null)
                            Text(
                                text = "Foto",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                // abrir archivos
                IconButton(
                    onClick = { documentos = !documentos },
                    modifier = Modifier
                        .width(75.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoLibrary,
                            contentDescription = null
                        )
                        /*Text(
                            text = "Galeria",
                            style = MaterialTheme.typography.bodySmall
                        )*/
                    }
                }

                Spacer(modifier = Modifier.size(16.dp))

                // guardar tarea
                if(!documentos){
                    IconButton(
                        onClick = {
                            if(!update){
                                // guardar las imagenes tomadas en la galeria
                                listImageUri.forEachIndexed { _, uri ->
                                    saveImageToGallery(
                                        uri,
                                        context,
                                        "image_${title}-${LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))}"
                                    )
                                }
                                listVideoUri.forEachIndexed { _, uri ->
                                    saveVideoToGallery(
                                        uri,
                                        context,
                                        "video_${title}-${LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))}"
                                    )
                                }

                                // crear el item para guardar la tarea
                                val item = WorksData(
                                    id = 0,
                                    titlework = title,
                                    descwork = desc,
                                    datework = date,
                                    hour = hourSelected,
                                    images = listImageUri,
                                    videos = listVideoUri,
                                    audios = listAudioUri,
                                    files = listFileUri
                                )

                                // generar la alarma para la tarea


                                // agregar tarea a la base de datos
                                multiViewModel.addWork(item)
                            } else {
                                val item = WorksData(
                                    id = tarea.id,
                                    titlework = title,
                                    descwork = desc,
                                    datework = date,
                                    hour = hourSelected,
                                    images = listImageUri,
                                    videos = listVideoUri,
                                    audios = listAudioUri,
                                    files = listFileUri
                                )
                                if(title != tarea.titlework || desc != tarea.descwork || hourSelected != tarea.hour || date != tarea.datework){
                                    multiViewModel.updateWork(item)

                                }
                            }
                            onClick()
                        },
                        modifier = Modifier
                            .width(75.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Outlined.Check, contentDescription = null)
                         /*   Text(
                                text = "Guardar",
                                style = MaterialTheme.typography.bodySmall
                            )*/
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogSelectHour(
    onDismiss: () -> Unit,
    state: TimePickerState,
    selected: (LocalTime) -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card {
            Column(
                Modifier.padding(PaddingValues(16.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(state = state)
                Button(
                    onClick = {
                        selected(
                            LocalTime.of(
                                state.hour,
                                state.minute
                            )
                        )
                        onDismiss()
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = stringResource(id = R.string.btnAceptar))
                }
                TextButton(
                    onClick = {
                        onDismiss()
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = stringResource(id = R.string.btnCancelar))
                }
            }
        }
    }
}