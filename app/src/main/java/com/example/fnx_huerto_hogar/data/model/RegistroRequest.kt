package com.example.fnx_huerto_hogar.data.model

data class RegistroRequest(
    val nombre: String,
    val apellido: String,
    val email: String,
    val contrasenna: String,
    val telefono: String? = "",
    val direccion: String? = "",
    val comuna: String? = "",
    val region: String? = ""
)
