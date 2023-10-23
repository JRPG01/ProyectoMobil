package com.example.notas.Navegacion

sealed class Screean (
    val route: String
){
    object Notas : Screean(
        route = "notas"
    )
    object NotasPrincipalScreean : Screean(
        route = "notas_principal_screean"
    )
}