package com.example.appnotas.Navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appnotas.ViewModel.NotasViewModel
import com.example.appnotas.utils.MultiNavigationType

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(
    navHostController: NavHostController,
    multiViewModel: NotasViewModel,
    navigationType: MultiNavigationType
) {

    NavHost(navController = navHostController, startDestination = ItemsMenu.InicioScreen.route){
        composable(route = ItemsMenu.NotaScreen.route){
            NotaScreen(multiViewModel,navigationType)
        }

        composable(route = ItemsMenu.InicioScreen.route){
            InicioScreen(multiViewModel)
        }

        composable(route = ItemsMenu.TareaScreen.route){
            TareaScreen(multiViewModel,navigationType)
        }

        composable(route = ItemsMenu.AjusteScreen.route){
            AjusteScreen(multiViewModel)
        }
    }
}