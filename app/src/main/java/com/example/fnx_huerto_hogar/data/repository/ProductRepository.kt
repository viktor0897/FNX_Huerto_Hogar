package com.example.fnx_huerto_hogar.data.repository

import com.example.fnx_huerto_hogar.Remote.RetrofitInstance
import com.example.fnx_huerto_hogar.data.model.Product
import com.example.fnx_huerto_hogar.data.model.toEntity

class ProductRepository {
    private val api = RetrofitInstance.api

    suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val productsDto = api.getAllProducts()
            val products = productsDto.map { it.toEntity() }  // Convertir DTO a Entity
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductById(id: String): Result<Product> {
        return try {
            val productDto = api.getProductById(id)
            val product = productDto.toEntity()  // Convertir DTO a Entity
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductsByCategory(categoria: String): Result<List<Product>> {
        return try {
            val productsDto = api.getProductsByCategory(categoria)
            val products = productsDto.map { it.toEntity() }  // Convertir DTO a Entity
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            val productsDto = api.searchProducts(query)
            val products = productsDto.map { it.toEntity() }  // Convertir DTO a Entity
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductsWithStock(): Result<List<Product>> {
        return try {
            val productsDto = api.getProductsWithStock()
            val products = productsDto.map { it.toEntity() }  // Convertir DTO a Entity
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}