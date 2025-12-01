package com.example.fnx_huerto_hogar.ui.theme.viewModel

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fnx_huerto_hogar.data.model.User
import com.example.fnx_huerto_hogar.data.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(): ViewModel() {

    //Estados del viewmodel
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _loginUserSuccessful = MutableStateFlow(false)
    val loginUserSuccessful: StateFlow<Boolean> = _loginUserSuccessful.asStateFlow()

    //Actualian estado
    fun OnEmailChange(email: String){
        _email.value = email
        _errorMessage.value = null
    }

    fun OnPasswordChange(password: String){
        _password.value = password
        _errorMessage.value = null
    }

    //Función de Login
    fun login() {
        //Valido
        if (_email.value.isEmpty()) {
            _errorMessage.value = "El email es obligatorio"
            return
        }

        if (_password.value.isEmpty()) {
            _errorMessage.value = "La contraseña es obligatoria"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                delay(1500)
                val user = UserRepository.login(_email.value, _password.value)
                if (user != null) {
                    //Login Exitoso
                    _currentUser.value = user
                    _loginUserSuccessful.value = true

                    clearFields()
                } else {
                    //Credenciales incorrectas
                    _errorMessage.value = "Email o contraseña incorrectos"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al iniciar sesión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    //Funciones auxiliares
    fun clearFields(){
        _email.value = ""
        _password.value = ""
        _errorMessage.value = null
    }

    //función para resetear estados de navegación
    fun resetNavigationStates(){
        _loginUserSuccessful.value = false
        _loginUserSuccessful.value = false
    }
}