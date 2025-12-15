package com.example.fnx_huerto_hogar.data.model

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val usuario: UsuarioDto
)