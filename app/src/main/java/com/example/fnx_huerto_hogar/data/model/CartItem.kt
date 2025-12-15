package com.example.fnx_huerto_hogar.data.model

data class CartItem(
    val id: Long? = null,
    val productId: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val image: String,
    val subtotal: Double = 0.0
)