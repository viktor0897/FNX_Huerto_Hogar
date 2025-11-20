package com.example.fnx_huerto_hogar.data.model

data class Cart(
    val id: Long = 0,
    val quantity: Int,
    val dateAdded: Long = System.currentTimeMillis()
)