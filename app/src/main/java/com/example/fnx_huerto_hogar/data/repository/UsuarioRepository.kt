// data/repository/UsuarioRepositorySimple.kt
package com.example.fnx_huerto_hogar.data.repository

import com.example.fnx_huerto_hogar.Remote.RetrofitInstance
import com.example.fnx_huerto_hogar.data.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UsuarioRepository {

    private val apiService = RetrofitInstance.api

    private var profilePictureUri: String? = null

    private var currentUser: Usuario? = null

    // 1. REGISTRO
    suspend fun registrarUsuario(
        nombre: String,
        apellido: String,
        email: String,
        contrasenna: String
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val usuario = Usuario(
                nombre = nombre,
                apellido = apellido,
                email = email,
                contrasenna = contrasenna
            )

            val response = apiService.registrarUsuario(usuario)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    // 3. LOGIN (con datos del usuario)
    suspend fun loginConUsuario(email: String, contrasenna: String): Usuario? = withContext(Dispatchers.IO) {
        return@withContext try {
            val loginData = mapOf(
                "email" to email,
                "contrasenna" to contrasenna
            )

            val response = apiService.login(loginData)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val usuarioMap = body["usuario"] as? Map<*, *>

                if (usuarioMap != null) {
                    val usuario = mapToUsuario(usuarioMap)
                    currentUser = usuario
                    usuario
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // 4. RECUPERAR CONTRASEÑA
    suspend fun recuperarContrasenna(email: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val emailData = mapOf("email" to email)
            val response = apiService.recuperarContrasenna(emailData)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    // 5. ACTUALIZAR CONTRASEÑA
    suspend fun actualizarContrasenna(
        email: String,
        codigo: String,
        nuevaContrasenna: String
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val datos = mapOf(
                "email" to email,
                "codigo" to codigo,
                "contrasennaNueva" to nuevaContrasenna
            )

            val response = apiService.actualizarContrasenna(datos)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    // 6. OBTENER USUARIO ACTUAL (guardado en memoria)
    fun getCurrentUser(): Usuario? = currentUser

    // 7. CERRAR SESIÓN
    fun logout() {
        currentUser = null
        profilePictureUri = null
    }

    // 8. VERIFICAR SI HAY SESIÓN
    fun isLoggedIn(): Boolean = currentUser != null

    // 9. GUARDAR FOTO DE PERFIL (LOCAL)
    fun updateProfilePicture(uriString: String) {
        profilePictureUri = uriString
    }

    // 10. OBTENER URI DE FOTO DE PERFIL
    fun getProfilePictureUri(): String? = profilePictureUri

    // 11. LIMPIAR FOTO DE PERFIL
    fun clearProfilePicture() {
        profilePictureUri = null
    }

    suspend fun actualizarUsuario(id: Long, usuarioActualizado: Usuario): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.actualizarUsuario(id, usuarioActualizado)
            if (response.isSuccessful && response.body() != null) {
                // Actualizar usuario en memoria
                currentUser = response.body()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    // ========== MÉTODO PARA CAMBIAR EMAIL (USANDO EL ANTERIOR) ==========

    suspend fun updateUserEmail(
        oldEmail: String,
        newEmail: String,
        password: String
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            // 1. Verificar que el usuario existe y la contraseña es correcta
            val usuario = loginConUsuario(oldEmail, password)
            if (usuario == null) return@withContext false

            // 2. Obtener el ID del usuario
            val id = usuario.id ?: return@withContext false

            // 3. Crear usuario actualizado
            val usuarioActualizado = usuario.copy(email = newEmail)

            // 4. Llamar al endpoint
            actualizarUsuario(id, usuarioActualizado)
        } catch (e: Exception) {
            false
        }
    }

    // ========== MÉTODO PARA CAMBIAR CONTRASEÑA ==========

    suspend fun updateUserPassword(
        email: String,
        currentPassword: String,
        newPassword: String
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            // 1. Verificar contraseña actual
            val usuario = loginConUsuario(email, currentPassword)
            if (usuario == null) return@withContext false

            // 2. Obtener ID
            val id = usuario.id ?: return@withContext false

            // 3. Crear usuario con nueva contraseña
            val usuarioActualizado = usuario.copy(contrasenna = newPassword)

            // 4. Llamar al endpoint
            actualizarUsuario(id, usuarioActualizado)
        } catch (e: Exception) {
            false
        }
    }


    // ========== METODO PRIVADO ==========
    private fun mapToUsuario(map: Map<*, *>): Usuario {
        return Usuario(
            id = (map["id"] as? Number)?.toLong(),
            nombre = map["nombre"] as? String ?: "",
            apellido = map["apellido"] as? String ?: "",
            email = map["email"] as? String ?: "",
            telefono = map["telefono"] as? String,
            direccion = map["direccion"] as? String,
            comuna = map["comuna"] as? String,
            region = map["region"] as? String,
            rol = map["rol"] as? String
        )
    }
}
