package com.example.fnx_huerto_hogar.data.model

import com.example.fnx_huerto_hogar.R
import com.example.fnx_huerto_hogar.data.model.Product

data class ProductDto(
    val id: String,
    val name: String,
    val price: Double,
    val category: String,
    val stock: Int,
    val description: String?,
    val image: String?,  // URL del backend
    val measure: String?,
    val origin: String?
)

// Función de conversión de DTO a Entity (modelo local)
fun ProductDto.toEntity(): Product {
    return Product(
        id = id,
        name = name,
        price = price,
        category = category,
        stock = stock,
        description = description,
        image = mapImagenLocal(image, id),  // Mapear a drawable local
        measure = measure,
        origin = origin
    )
}

// Mapeo de imagen: convierte nombre/URL a drawable resource
private fun mapImagenLocal(img: String?, id: String): Int {
    // Intentar extraer nombre del archivo de la URL
    val nombreImagen = img?.substringAfterLast("/")?.substringBeforeLast(".")?.lowercase()

    return when (nombreImagen) {
        "manzanas" -> R.drawable.manzana_fuji
        "naranjas" -> R.drawable.naranja_valencia
        "platanos" -> R.drawable.platano
        "zanahorias" -> R.drawable.zanahorias
        "espinacas" -> R.drawable.espinacas
        "miel" -> R.drawable.miel
        "leche" -> R.drawable.leche_entera
        else -> when (id) {  // Fallback por ID
            "FR001" -> R.drawable.manzana_fuji
            "FR002" -> R.drawable.naranja_valencia
            "FR003" -> R.drawable.platano
            "VR001" -> R.drawable.zanahorias
            "VR002" -> R.drawable.espinacas
            "PO001" -> R.drawable.miel
            "PL001" -> R.drawable.leche_entera
            else -> R.drawable.ic_launcher_foreground  // Imagen por defecto
        }
    }
}