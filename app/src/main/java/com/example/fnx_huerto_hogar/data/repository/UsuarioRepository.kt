package com.example.fnx_huerto_hogar.data.repository

import com.example.fnx_huerto_hogar.Remote.RetrofitInstance
import com.example.fnx_huerto_hogar.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception

class UsuarioRepository {

    object CurrentUser {
        var user: Usuario? = null
        var profilePictureUri: String? = null

        fun get(): Usuario? = user
        fun save(usuario: Usuario) { user = usuario }
        fun clear() {
            user = null
            profilePictureUri = null
        }

        fun updateProfilePicture(uri: String) {
            profilePictureUri = uri
            // Actualizar también el usuario si existe
            user = user?.copy(profilePicture = uri)
        }

        fun isLoggedIn(): Boolean = user != null

    }

    private val apiService = RetrofitInstance.api

    // ========== FUNCIONES PRINCIPALES ==========

    /**
     * Registrar un nuevo usuario
     */
    suspend fun registrarUsuario(usuario: Usuario): Resultado<Usuario> = withContext(Dispatchers.IO) {
        return@withContext try {
            // Convertir Usuario a RegistroRequest
            val registroRequest = RegistroRequest(
                nombre = usuario.nombre,
                apellido = usuario.apellido,
                email = usuario.email,
                contrasenna = usuario.contrasenna ?: "",
                telefono = usuario.telefono ?: "",
                direccion = usuario.direccion ?: "",
                comuna = usuario.comuna ?: "",
                region = usuario.region ?: ""
            )

            val response = apiService.registrarUsuario(registroRequest)

            if (response.isSuccessful) {
                val usuarioDto = response.body()
                if (usuarioDto != null) {
                    // Convertir UsuarioDto a Usuario
                    val usuarioRegistrado = Usuario(
                        id = usuarioDto.id,
                        nombre = usuarioDto.nombre,
                        apellido = usuarioDto.apellido,
                        email = usuarioDto.email,
                        contrasenna = null, // No guardamos la contraseña
                        telefono = usuarioDto.telefono,
                        direccion = usuarioDto.direccion,
                        comuna = usuarioDto.comuna,
                        region = usuarioDto.region,
                        rol = usuarioDto.rol,
                        profilePicture = usuarioDto.profilePicture
                    )
                    Resultado.Exito(usuarioRegistrado)
                } else {
                    Resultado.Error(Exception("Respuesta vacía del servidor"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Resultado.Error(Exception("Error al registrar: $errorBody"))
            }
        } catch (e: Exception) {
            Resultado.Error(e)
        }
    }

    /**
     * Iniciar sesión
     */
    suspend fun iniciarSesion(email: String, contrasenna: String): Resultado<Usuario> = withContext(Dispatchers.IO) {
        return@withContext try {
            val loginRequest = LoginRequest(email = email, contrasenna = contrasenna)
            val response = apiService.iniciarSesion(loginRequest)

            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    // Convertir UsuarioDto a Usuario
                    val usuario = Usuario(
                        id = loginResponse.usuario.id,
                        nombre = loginResponse.usuario.nombre,
                        apellido = loginResponse.usuario.apellido,
                        email = loginResponse.usuario.email,
                        contrasenna = null, // No guardamos la contraseña
                        telefono = loginResponse.usuario.telefono,
                        direccion = loginResponse.usuario.direccion,
                        comuna = loginResponse.usuario.comuna,
                        region = loginResponse.usuario.region,
                        rol = loginResponse.usuario.rol,
                        profilePicture = loginResponse.usuario.profilePicture
                    )

                    // Guardar el usuario en CurrentUser
                    CurrentUser.save(usuario)

                    Resultado.Exito(usuario)
                } else {
                    Resultado.Error(Exception("Respuesta vacía del servidor"))
                }
            } else {
                // Leer el mensaje de error específico
                val errorBody = response.errorBody()?.string()
                val errorMessage = when {
                    errorBody?.contains("Email o contraseña incorrectos") == true -> "Credenciales incorrectas"
                    errorBody?.contains("error") == true -> {
                        // Extraer mensaje de error del JSON
                        try {
                            val errorJson = android.util.JsonReader(java.io.StringReader(errorBody))
                            var errorMsg = "Error de autenticación"
                            errorJson.beginObject()
                            while (errorJson.hasNext()) {
                                if (errorJson.nextName() == "error") {
                                    errorMsg = errorJson.nextString()
                                    break
                                } else {
                                    errorJson.skipValue()
                                }
                            }
                            errorJson.endObject()
                            errorMsg
                        } catch (e: Exception) {
                            "Error de autenticación"
                        }
                    }
                    else -> "Error de conexión: ${response.code()}"
                }
                Resultado.Error(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Resultado.Error(Exception("Error de red: ${e.message}"))
        }
    }

    /**
     * Buscar usuario por email
     */
    suspend fun buscarPorEmail(email: String): Resultado<Usuario> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.buscarPorEmail(email)

            if (response.isSuccessful) {
                val usuarioDto = response.body()
                if (usuarioDto != null) {
                    val usuario = Usuario(
                        id = usuarioDto.id,
                        nombre = usuarioDto.nombre,
                        apellido = usuarioDto.apellido,
                        email = usuarioDto.email,
                        contrasenna = null,
                        telefono = usuarioDto.telefono,
                        direccion = usuarioDto.direccion,
                        comuna = usuarioDto.comuna,
                        region = usuarioDto.region,
                        rol = usuarioDto.rol,
                        profilePicture = usuarioDto.profilePicture
                    )
                    Resultado.Exito(usuario)
                } else {
                    Resultado.Error(Exception("Usuario no encontrado"))
                }
            } else if (response.code() == 404) {
                Resultado.Error(Exception("Usuario no encontrado"))
            } else {
                Resultado.Error(Exception("Error al buscar usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resultado.Error(e)
        }
    }

    /**
     * Verificar si un email ya existe
     */
    suspend fun verificarEmailExistente(email: String): Resultado<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.verificarEmail(email)

            if (response.isSuccessful) {
                val result = response.body()
                val existe = result?.get("existe") as? Boolean ?: false
                Resultado.Exito(existe)
            } else {
                Resultado.Error(Exception("Error al verificar email"))
            }
        } catch (e: Exception) {
            Resultado.Error(e)
        }
    }

    /**
     * Actualizar contraseña
     */
    suspend fun actualizarContrasenna(id: Long, nuevaContrasenna: String): Resultado<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            val request = mapOf("contrasenna" to nuevaContrasenna)
            val response = apiService.actualizarContrasenna(id, request)

            if (response.isSuccessful) {
                val result = response.body()
                val success = result?.get("success") as? Boolean ?: false
                if (success) {
                    Resultado.Exito(true)
                } else {
                    Resultado.Error(Exception("No se pudo actualizar la contraseña"))
                }
            } else {
                Resultado.Error(Exception("Error al actualizar contraseña: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resultado.Error(e)
        }
    }

    /**
     * Actualizar correo electrónico
     */
    suspend fun actualizarCorreo(id: Long, nuevoCorreo: String): Resultado<Usuario> = withContext(Dispatchers.IO) {
        return@withContext try {
            val request = mapOf("email" to nuevoCorreo)
            val response = apiService.actualizarCorreo(id, request)

            if (response.isSuccessful) {
                val usuarioDto = response.body()
                if (usuarioDto != null) {
                    val usuario = Usuario(
                        id = usuarioDto.id,
                        nombre = usuarioDto.nombre,
                        apellido = usuarioDto.apellido,
                        email = usuarioDto.email,
                        contrasenna = null,
                        telefono = usuarioDto.telefono,
                        direccion = usuarioDto.direccion,
                        comuna = usuarioDto.comuna,
                        region = usuarioDto.region,
                        rol = usuarioDto.rol,
                        profilePicture = usuarioDto.profilePicture
                    )
                    Resultado.Exito(usuario)
                } else {
                    Resultado.Error(Exception("Respuesta vacía del servidor"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Resultado.Error(Exception("Error: $errorBody"))
            }
        } catch (e: Exception) {
            Resultado.Error(e)
        }
    }

    /**
     * Eliminar usuario
     */
    suspend fun eliminarUsuario(id: Long): Resultado<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.eliminarUsuario(id)

            if (response.isSuccessful) {
                val result = response.body()
                val success = result?.get("success") as? Boolean ?: false
                if (success) {
                    Resultado.Exito(true)
                } else {
                    Resultado.Error(Exception("No se pudo eliminar el usuario"))
                }
            } else {
                Resultado.Error(Exception("Error al eliminar usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resultado.Error(e)
        }
    }


    /**
     * Registrar usuario si no existe
     */
    suspend fun registrarSiNoExiste(usuario: Usuario): Resultado<Usuario> {
        val existeResultado = verificarEmailExistente(usuario.email)

        return when (existeResultado) {
            is Resultado.Exito -> {
                if (existeResultado.datos) {
                    // Si existe, obtener el usuario
                    buscarPorEmail(usuario.email)
                } else {
                    // Si no existe, registrarlo
                    registrarUsuario(usuario)
                }
            }
            is Resultado.Error -> {
                existeResultado
            }
        }
    }

    /**
     * Cerrar sesión (local)
     */
    fun cerrarSesion(): Resultado<Boolean> {
        // Aquí normalmente limpiarías SharedPreferences, etc.
        return Resultado.Exito(true)
    }

    /**
     * Guardar usuario en preferencias (ejemplo)
     */
    suspend fun guardarUsuarioLocal(usuario: Usuario): Resultado<Boolean> {
        return try {
            // Aquí implementarías el guardado en SharedPreferences o Room
            Resultado.Exito(true)
        } catch (e: Exception) {
            Resultado.Error(e)
        }
    }

    /**
     * Obtener usuario guardado localmente
     */
    suspend fun obtenerUsuarioLocal(): Resultado<Usuario?> {
        return try {
            // Aquí implementarías la obtención desde SharedPreferences o Room
            Resultado.Exito(null)
        } catch (e: Exception) {
            Resultado.Error(e)
        }
    }
}

// ========== CLASES PARA MANEJAR RESULTADOS ==========

sealed class Resultado<out T> {
    data class Exito<out T>(val datos: T) : Resultado<T>()
    data class Error(val excepcion: Exception) : Resultado<Nothing>()

    fun isExito(): Boolean = this is Exito
    fun isError(): Boolean = this is Error

    fun getDatosOrNull(): T? = when (this) {
        is Exito -> datos
        is Error -> null
    }

    fun getErrorOrNull(): Exception? = when (this) {
        is Exito -> null
        is Error -> excepcion
    }

    fun onExito(accion: (T) -> Unit): Resultado<T> {
        if (this is Exito) accion(datos)
        return this
    }

    fun onError(accion: (Exception) -> Unit): Resultado<T> {
        if (this is Error) accion(excepcion)
        return this
    }
}