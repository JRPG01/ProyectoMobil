package com.example.notas.ui.theme

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.notas.Navegacion.Screean
import com.example.notas.data.Notas
import com.example.notas.data.NotasDatabase
import kotlinx.coroutines.launch

object EditNoteDestination {
    const val itemIdArg = "itemId"
    val routeWithArgs = "notas_editar/{$itemIdArg}"
}
class PantallaEdit:ComponentActivity(){
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
                    VistaEdit(navHostController = rememberNavController())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VistaEdit(navHostController: NavHostController){
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    val scope = rememberCoroutineScope()
    val c = LocalContext.current
    val viewModel = EditViewModel()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.padding(5.dp),
        topBar={
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigate(Screean.NotasPrincipalScreean.route) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription= null)
                    }
                },
                title = {},
                actions = { IconButton(onClick = { coroutineScope.launch{
                    val db = Room.databaseBuilder(c, NotasDatabase::class.java, "notas_database")
                        .build()
                    val g = Notas(0, viewModel.currentTitulo, viewModel.currentNota, "",)
                    db.NotaDao().update(g)
                }
                    navHostController.popBackStack()
                }) {

                }}
            )
            Row(verticalAlignment = Alignment.CenterVertically){}
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add files") },
                icon = { Icon(Icons.Filled.AttachFile, contentDescription = "") },
                onClick = {
                    showBottomSheet = true
                }
            )
        }
    ) {contentPadding ->
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                // Sheet content
                val context = LocalContext.current

                //Boton de Fotos
                TextButton(onClick = {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    }
                }) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = "Photo")
                        Text(text = " Foto")
                    }
                }

                //Boton de Video
                TextButton(onClick = {
                    val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    }
                }) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Videocam, contentDescription = "Video")
                        Text(text = " Video")
                    }
                }

                //Boton de Audio
                TextButton(onClick = {
                    val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    }
                }) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Mic, contentDescription = "Audio")
                        Text(text = " Audio")
                    }
                }

                //Boton de Documentos
                TextButton(onClick = {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "*/*"
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    }
                }) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.FileOpen, contentDescription = "Document")
                        Text(text = " Documentos")
                    }
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(contentPadding)) {
            titulo(name = viewModel.currentTitulo, changeName = {viewModel.updateNote(it)},modifier =Modifier)
            Content(contenido = viewModel.currentNota, onChangue = {viewModel.updateNote(it)},modifier =Modifier)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(contenido: String,onChangue:(String)->Unit,modifier: Modifier){
    TextField(
       value = contenido,
       onValueChange = onChangue,
       modifier = Modifier
           .fillMaxWidth()
           .fillMaxHeight()
   )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun titulo(name: String,
    changeName:  (String) -> Unit,
    modifier : Modifier){
    Box(){
        TextField(value = name, onValueChange =changeName)
    }
}

