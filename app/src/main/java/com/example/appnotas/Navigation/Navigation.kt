package com.example.appnotas.Navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appnotas.ViewModel.NotasViewModel
import com.example.appnotas.utils.MultiNavigationType
import com.example.appnotas.NotaScreen
import com.example.appnotas.InicioScreen
import com.example.appnotas.TareaScreen
import com.example.appnotas.AjusteScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(
    navHostController: NavHostController,
    multiViewModel: NotasViewModel,
    navigationType: MultiNavigationType
) {

    NavHost(navController = navHostController, startDestination = ItemsMenu.NotaScreen.route){
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