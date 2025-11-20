package com.example.fnx_huerto_hogar.data.repository

import com.example.fnx_huerto_hogar.data.model.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

object CartRepository {

    // Estado
    private val cartItemsState = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = cartItemsState.asStateFlow()

    fun getCart(): Flow<List<CartItem>> = cartItemsState

    suspend fun addToCart(productoId: String, name: String, price: Double, image: Int, quantity: Int): Boolean {
        try {
            val actualItems = cartItemsState.value.toMutableList()
            val existingItem = actualItems.find { it.productId == productoId }

            if (existingItem != null) {
                // Si ya existe, actualizar cantidad
                val newQuantity = existingItem.quantity + quantity
                updateQuantity(productoId, newQuantity)
            } else {
                // Si no existe, agregar nuevo item
                val nuevoItem = CartItem(
                    productId = productoId,
                    name = name,
                    price = price,
                    quantity = quantity,
                    image = image
                )
                actualItems.add(nuevoItem)
                cartItemsState.value = actualItems
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun updateQuantity(productoId: String, cantidad: Int) {
        if (cantidad <= 0) {
            deleteFromCart(productoId)
        } else {
            val actualItems = cartItemsState.value.toMutableList()
            val itemIndex = actualItems.indexOfFirst { it.productId == productoId }

            if (itemIndex != -1) {
                val actualItem = actualItems[itemIndex]
                actualItems[itemIndex] = actualItem.copy(quantity = cantidad)
                cartItemsState.value = actualItems
            }
        }
    }

    suspend fun deleteFromCart(productoId: String) {
        val updatedItems = cartItemsState.value.filterNot { it.productId == productoId }
        cartItemsState.value = updatedItems
    }

    suspend fun emptyCart() {
        cartItemsState.value = emptyList()
    }

    fun getTotalItems(): Flow<Int> = cartItemsState.map { items ->
        items.sumOf { it.quantity }
    }

    fun calculateCartTotal(): Flow<Double> {
        return cartItemsState.map { items ->
            items.sumOf { it.subtotal }
        }
    }
}
