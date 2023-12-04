package com.example.appnotas.Navigation


sealed class ItemsMenu(
    val route: String
){
    object InicioScreen : ItemsMenu(
        route = "inicio_screen"
    )

    object TareaScreen : ItemsMenu(
        route = "tareas_screen"
    )

    object NotaScreen : ItemsMenu(
        route = "notas_screen"
    )

    object AjusteScreen : ItemsMenu(
        route = "ajustes_screen"
    )
}