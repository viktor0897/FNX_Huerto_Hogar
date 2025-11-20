package com.example.fnx_huerto_hogar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey
    val id: String,
    val name: String,
    val price: Double,
    val category: ProductCategory,
    val stock: Int,
    val description: String,
    val image: Int,
    val measure: String,
    val origin: String = ""
) {
    fun stockAvailable(): Boolean = stock > 0
    fun stockSufficient(quantity: Int) = stock >= quantity
}

enum class ProductCategory(
    categoryName: String
){
    FRUTAS("Frutas"),
    VERDURAS("Verduras"),
    ORGANICOS("Orgánicos")
}

