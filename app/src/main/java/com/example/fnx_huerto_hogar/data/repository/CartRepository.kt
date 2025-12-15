package com.example.fnx_huerto_hogar.data.repository

import com.example.fnx_huerto_hogar.Remote.RetrofitInstance
import com.example.fnx_huerto_hogar.data.model.AddToCartRequest
import com.example.fnx_huerto_hogar.data.model.Cart

class CartRepository {
    private val api = RetrofitInstance.api

    suspend fun getCart(usuarioId: Long): Result<Cart> {
        return try {
            val cart = api.getCart(usuarioId)
            Result.success(cart)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addToCart(usuarioId: Long, productoId: String, cantidad: Int): Result<Cart> {
        return try {
            val request = AddToCartRequest(productId = productoId, quantity = cantidad)
            val cart = api.addToCart(usuarioId, request)
            Result.success(cart)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun updateCartItem(usuarioId: Long, productoId: String, cantidad: Int): Result<Cart> {
        return try {
            val request = mapOf("quantity" to cantidad)
            val cart = api.updateCartItem(usuarioId, productoId, request)
            Result.success(cart)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun removeFromCart(usuarioId: Long, productoId: String): Result<Cart> {
        return try {
            val cart = api.removeFromCart(usuarioId, productoId)
            Result.success(cart)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clearCart(usuarioId: Long): Result<Unit> {
        return try {
            api.clearCart(usuarioId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}