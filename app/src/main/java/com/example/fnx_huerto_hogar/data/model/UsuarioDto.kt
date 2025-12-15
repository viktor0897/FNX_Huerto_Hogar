package com.example.fnx_huerto_hogar.data.model

data class UsuarioDto(
    val id: Long,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String?,      // Nullable
    val direccion: String?,     // Nullable
    val comuna: String?,        // Nullable
    val region: String?,        // Nullable
    val rol: String,
    val profilePicture: String? // Nullable
)