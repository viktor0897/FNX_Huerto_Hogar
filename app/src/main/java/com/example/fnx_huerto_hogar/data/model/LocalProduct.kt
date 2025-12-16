package com.example.fnx_huerto_hogar.data.model

import com.example.fnx_huerto_hogar.R

data class ProductoDestacado(
    val id: String,
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val imagenRes: Int,  // Drawable resource
    val categoria: String
)

object ProductosDestacadosData {
    val productos = listOf(
        ProductoDestacado(
            id = "1",
            nombre = "Manzana Fuji",
            precio = 2500.0,
            stock = 50,
            imagenRes = R.drawable.manzana_fuji,
            categoria = "FRUTAS"
        ),
        ProductoDestacado(
            id = "2",
            nombre = "Naranja Valencia",
            precio = 1800.0,
            stock = 40,
            imagenRes = R.drawable.naranja_valencia,
            categoria = "FRUTAS"
        ),
        ProductoDestacado(
            id = "3",
            nombre = "Zanahorias",
            precio = 1200.0,
            stock = 60,
            imagenRes = R.drawable.zanahorias,
            categoria = "VERDURAS"
        ),
        ProductoDestacado(
            id = "4",
            nombre = "Plátano",
            precio = 1500.0,
            stock = 45,
            imagenRes = R.drawable.platano,
            categoria = "FRUTAS"
        ),
        ProductoDestacado(
            id = "5",
            nombre = "Espinacas",
            precio = 2000.0,
            stock = 30,
            imagenRes = R.drawable.espinacas,
            categoria = "VERDURAS"
        )
    )
}