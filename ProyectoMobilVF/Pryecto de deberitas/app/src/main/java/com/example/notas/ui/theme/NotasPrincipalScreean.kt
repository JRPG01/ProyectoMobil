package com.example.notas.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.notas.Navegacion.Screean
import com.example.notas.R
import com.example.notas.data.Notas
import com.example.notas.data.NotasDatabase
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarExample(searchChanged: (String) -> Unit,
                     currentSearch:String,
                     modifier: Modifier = Modifier) {


    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = currentSearch,
                    onValueChange = searchChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Icon(Icons.Default.Search, contentDescription = "Search")

                    }
                )

            }        },
        modifier = modifier
    )
}
@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldExample(navController: NavController,
                    noteViewModel: NotesViewModel= viewModel()
) {
    val noteUiState by noteViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    // variable y lista
    val c = LocalContext.current
    var listnote = mutableListOf<Notas>()
    val db = Room.databaseBuilder(c, NotasDatabase::class.java, "notas_database").allowMainThreadQueries().build()
    listnote = db.NotaDao().getAllNotas().toMutableList()
    // variable y lista
    Scaffold(

        topBar = {
            TopAppBarExample(searchChanged = {noteViewModel.updateUserGuess(it)},
                currentSearch = noteViewModel.currentSearch)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screean.Notas.route) }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            //carga de la lista
            scope.launch {
                db.NotaDao().getNotas()
            }
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .padding(bottom = 90.dp)){
                    itemsIndexed(listnote){pos, w ->
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(100.dp)
                            .clickable { navController.navigate("/${w.id}") }){
                            Text(text ="${w.title}", maxLines = 1, style = MaterialTheme.typography.titleLarge)
                            Text(text ="${w.body}", maxLines = 1, style=MaterialTheme.typography.bodyMedium)

                            IconButton(modifier = Modifier.padding(100.dp),onClick = {}) {
                                Icon(painter= painterResource(id= R.drawable.baseline_delete), contentDescription = "Borrar",tint= MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                    }
                }

            }
            //carga de la lista
        }
    }


