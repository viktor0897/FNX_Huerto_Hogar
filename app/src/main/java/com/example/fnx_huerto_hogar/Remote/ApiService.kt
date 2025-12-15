package com.example.fnx_huerto_hogar.data.remote

import com.example.fnx_huerto_hogar.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ========== USER ENDPOINTS ==========
    @GET("api/usuario/buscar")
    suspend fun buscarPorEmail(@Query("email") email: String): Response<UsuarioDto>

    @POST("api/usuario/registrar")
    suspend fun registrarUsuario(@Body request: RegistroRequest): Response<UsuarioDto>

    @POST("api/usuario/login")
    suspend fun iniciarSesion(@Body request: LoginRequest): Response<LoginResponse>

    @PUT("api/usuario/{id}/contrasenna")
    suspend fun actualizarContrasenna(
        @Path("id") id: Long,
        @Body request: Map<String, String>
    ): Response<Map<String, Any>>

    @PUT("api/usuario/{id}/correo")
    suspend fun actualizarCorreo(
        @Path("id") id: Long,
        @Body request: Map<String, String>
    ): Response<UsuarioDto>

    @DELETE("api/usuario/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Long): Response<Map<String, Any>>

    @GET("api/usuario/verificar-email")
    suspend fun verificarEmail(@Query("email") email: String): Response<Map<String, Any>>

    // ========== PRODUCT ENDPOINTS ==========
    @GET("api/products")
    suspend fun getAllProducts(): List<Product>

    @GET("api/products/{id}")
    suspend fun getProductById(@Path("id") id: String): Product

    @GET("api/products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): List<Product>

    @GET("api/products/search")
    suspend fun searchProducts(@Query("name") name: String): List<Product>

    @GET("api/products/stock")
    suspend fun getProductsWithStock(): List<Product>

    @POST("api/products")
    suspend fun createProduct(@Body product: Product): Product

    @PUT("api/products/{id}")
    suspend fun updateProduct(@Path("id") id: String, @Body product: Product): Product

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: String): Response<Unit>


    // ========== CART ENDPOINTS ==========
    @GET("api/cart/{userId}")
    suspend fun getCart(@Path("userId") userId: Long): Cart

    @POST("api/cart/{userId}/add")
    suspend fun addToCart(
        @Path("userId") userId: Long,
        @Body request: AddToCartRequest
    ): Cart

    @PUT("api/cart/{userId}/update/{productId}")
    suspend fun updateCartItem(
        @Path("userId") userId: Long,
        @Path("productId") productId: String,
        @Body request: Map<String, Int>  // {"quantity": 5}
    ): Cart

    @DELETE("api/cart/{userId}/remove/{productId}")
    suspend fun removeFromCart(
        @Path("userId") userId: Long,
        @Path("productId") productId: String
    ): Cart

    @DELETE("api/cart/{userId}/clear")
    suspend fun clearCart(@Path("userId") userId: Long): Response<Unit>
}