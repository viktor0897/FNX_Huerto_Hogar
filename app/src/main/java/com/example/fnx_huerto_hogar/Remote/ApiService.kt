package com.example.fnx_huerto_hogar.Remote

import com.example.fnx_huerto_hogar.data.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // 1. REGISTRO (público)
    @POST("api/usuario/guardar")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>

    // 2. LOGIN (público) - Solo necesitas email y contraseña
    @POST("api/usuario/login")
    suspend fun login(@Body loginData: Map<String, String>): Response<Map<String, Any>>

    // 3. RECUPERAR CONTRASEÑA (público)
    @POST("api/usuario/recuperar-contrasenna")
    suspend fun recuperarContrasenna(@Body emailData: Map<String, String>): Response<String>

    // 4. ACTUALIZAR CONTRASEÑA (público)
    @PUT("api/usuario/actualizar-contrasenna")
    suspend fun actualizarContrasenna(@Body datos: Map<String, String>): Response<String>

    //5. ACTUALIZAR USUARIO
    @PUT("api/usuario/actualizar/{id}")
    suspend fun actualizarUsuario(@Path("id")id: Long,
                                  @Body usuario: Usuario): Response<Usuario>
}