package com.example.fnx_huerto_hogar.data.repository

import com.example.fnx_huerto_hogar.data.model.User
import com.example.fnx_huerto_hogar.data.model.UserRole
import kotlinx.coroutines.delay

object UserRepository {

    //Variable que guarda el usuario actual
    private var currentUser: User? = null
        private set //Todos pueden ver quien vive en la casa (CurrentUser) Pero solo el dueño puede
                    //Cambiar quien vive en la casa (modificarlo)

    //Registrao
    suspend fun registerUser(user: User): Boolean {
        delay(1000)
        return try {
            val userExist = users.find { it.email == user.email }
            if (userExist != null) {
                return false // Usuario Existe
            } else {
                users.add(user)
                return true
            }
        } catch (e: Exception) {
            false
        }
    }

    //Login
    suspend fun login(email: String, password: String): User? {
        delay(800)
        val user = users.find { it.email == email && it.password == password }
        if (user != null) {
            currentUser = user
        }
        return user
    }

    fun logout(){
        currentUser = null
    }

    //Coger usuario actual
    fun getCurrentUser(): User? = currentUser

    //Coger todos
    suspend fun getAllUsers(): List<User> {
        delay(500)
        return users.toList()
    }

    //Deletear
    suspend fun deleteUser(email: String): Boolean {
        delay(300)
        return try {
            val userExist = users.find { it.email == email }
            if (userExist != null) {
                users.remove(userExist)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    //Cogerlos por Email
    suspend fun getUserByEmail(email: String): User?{
        delay(400)
        return users.find {it.email == email}
    }

    //Lista de usuarios seteados
    private val users = mutableListOf<User>(
        User(
            email = "bastian@duoc.cl",
            name = "Bastian",
            lastName = "Burgos",
            password = "123456",
            phone = "912345678",
            address = "Av. Principal 123",
            comuna = "Santiago",
            region = "Región Metropolitana",
            rol = UserRole.USER
        ),
        User(
            email = "eriberto@gmail.com",
            name = "Eriberto",
            lastName = "Moya",
            password = "123456789",
            phone = "987654321",
            address = "Calle Secundaria 456",
            comuna = "Providencia",
            region = "Región Metropolitana",
            rol = UserRole.USER
        )
    )
}