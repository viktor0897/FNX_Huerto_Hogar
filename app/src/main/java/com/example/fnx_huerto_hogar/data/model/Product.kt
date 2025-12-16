package com.example.fnx_huerto_hogar.data.model

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val category: String,
    val stock: Int,
    val description: String?,
    val image: Int?,
    val measure: String?,
    val origin: String?
) {
    fun stockAvailable(): Boolean = stock > 0
    fun stockSufficient(quantity: Int) = stock >= quantity
}