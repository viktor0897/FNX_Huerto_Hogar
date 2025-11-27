package com.example.fnx_huerto_hogar.data.model

data class CartItem(
    val productId: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val image: Int
){

    val subtotal: Double get() = price * quantity
}