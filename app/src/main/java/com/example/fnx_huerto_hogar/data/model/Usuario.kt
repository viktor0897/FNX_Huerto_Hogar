// data/model/Usuario.kt
package com.example.fnx_huerto_hogar.data.model

data class Usuario(
    val id: Long? = null,
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val contrasenna: String? = null,
    val telefono: String? = null,
    val direccion: String? = null,
    val comuna: String? = null,
    val region: String? = null,
    val rol: String? = null,
    val profilePicture: String? = null
)