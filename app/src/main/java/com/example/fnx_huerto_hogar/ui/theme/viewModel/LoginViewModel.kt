package com.example.fnx_huerto_hogar.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fnx_huerto_hogar.data.model.Usuario
import com.example.fnx_huerto_hogar.data.repository.Resultado
import com.example.fnx_huerto_hogar.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UsuarioRepository = UsuarioRepository()
): ViewModel() {

    // Estados del viewmodel
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser.asStateFlow()

    private val _loginUserSuccessful = MutableStateFlow(false)
    val loginUserSuccessful: StateFlow<Boolean> = _loginUserSuccessful.asStateFlow()

    // Actualizar estado
    fun OnEmailChange(email: String) {
        _email.value = email
        _errorMessage.value = null
    }

    fun OnPasswordChange(password: String) {
        _password.value = password
        _errorMessage.value = null
    }

    fun login() {
        // Validaciones
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
                val result = userRepository.iniciarSesion(_email.value, _password.value)

                when (result) {
                    is Resultado.Exito -> {
                        // El repositorio ya retorna el Usuario directamente
                        val usuario = result.datos

                        // Actualizar estados
                        _currentUser.value = usuario
                        _loginUserSuccessful.value = true
                        clearFields()

                        // El usuario ya está guardado en CurrentUser (lo hace el repositorio)
                    }
                    is Resultado.Error -> {
                        _errorMessage.value = result.excepcion.message ?: "Email o contraseña incorrectos"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Funciones auxiliares
    fun clearFields() {
        _email.value = ""
        _password.value = ""
        _errorMessage.value = null
    }

    // Función para resetear estados de navegación
    fun resetNavigationStates() {
        _loginUserSuccessful.value = false
    }

    // Función para cerrar sesión
    fun logout() {
        UsuarioRepository.CurrentUser.clear()
        _currentUser.value = null
        _loginUserSuccessful.value = false
        clearFields()
    }
}