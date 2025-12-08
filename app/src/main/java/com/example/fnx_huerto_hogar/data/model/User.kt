package com.example.fnx_huerto_hogar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class User(
    @PrimaryKey
    val email: String,
    val name: String,
    val lastName: String,
    val password: String,
    val phone: String,
    val address: String,
    val comuna: String,
    val region: String,
    var profilePicture: String? = null,
    val rol: UserRole = UserRole.USER
)

enum class UserRole {
    USER,
    ADMIN
}