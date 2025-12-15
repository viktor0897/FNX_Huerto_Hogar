package com.example.fnx_huerto_hogar.data.repository

import com.example.fnx_huerto_hogar.Remote.RetrofitInstance
import com.example.fnx_huerto_hogar.data.model.Product

class ProductRepository {
    private val api = RetrofitInstance.api

    suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val products = api.getAllProducts()
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductById(id: String): Result<Product> {
        return try {
            val product = api.getProductById(id)
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductsByCategory(categoria: String): Result<List<Product>> {
        return try {
            val products = api.getProductsByCategory(categoria)
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            val products = api.searchProducts(query)
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductsWithStock(): Result<List<Product>> {
        return try {
            val products = api.getProductsWithStock()
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
