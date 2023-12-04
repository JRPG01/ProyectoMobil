package com.example.notas.Navegacion

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.Composable
import androidx.navigation.navArgument
import com.example.notas.ui.theme.EditNoteDestination
import com.example.notas.ui.theme.EditViewModel
import com.example.notas.ui.theme.ScaffoldExample
import com.example.notas.ui.theme.ScaffoldNotas
import com.example.notas.ui.theme.VistaEdit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navegation(
    navHostController: NavHostController
){
    NavHost(navController = navHostController, startDestination = Screean.NotasPrincipalScreean.route){
        composable(route = Screean.Notas.route){
            ScaffoldNotas(navHostController)
        }
        composable(route = Screean.NotasPrincipalScreean.route){
            ScaffoldExample(navHostController)
        }
        composable(route = EditNoteDestination.routeWithArgs,
            arguments= listOf(navArgument(EditNoteDestination.itemIdArg){})){
            VistaEdit(navHostController)
        }
    }
}