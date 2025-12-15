package com.example.fnx_huerto_hogar.data.model

import java.time.LocalDateTime

data class Cart(
    val id: Long = 0,
    val userId: Long,
    val quantity: Int,
    val total: Double = 0.0,
    val dateAdded: String? = null,
    val items: List<CartItem> = emptyList()
)