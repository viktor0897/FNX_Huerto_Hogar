package com.example.fnx_huerto_hogar.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fnx_huerto_hogar.data.model.Usuario
import com.example.fnx_huerto_hogar.data.repository.UsuarioRepository.CurrentUser
import com.example.fnx_huerto_hogar.data.repository.Resultado
import com.example.fnx_huerto_hogar.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    // Estados para los campos
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone.asStateFlow()

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address.asStateFlow()

    private val _comuna = MutableStateFlow("")
    val comuna: StateFlow<String> = _comuna.asStateFlow()

    private val _region = MutableStateFlow("")
    val region: StateFlow<String> = _region.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    // Funciones para actualizar campos
    fun onNameChange(newName: String) {
        _name.value = newName
        _errorMessage.value = null
    }

    fun onLastNameChange(newLastName: String) {
        _lastName.value = newLastName
        _errorMessage.value = null
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _errorMessage.value = null
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        _errorMessage.value = null
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
        _errorMessage.value = null
    }

    fun onPhoneChange(newPhone: String) {
        _phone.value = newPhone
        _errorMessage.value = null
    }

    fun onAdressChange(newAdress: String) {
        _address.value = newAdress
        _errorMessage.value = null
    }

    fun onComunaChange(newComuna: String) {
        _comuna.value = newComuna
        _errorMessage.value = null
    }

    fun onRegionChange(newRegion: String) {
        _region.value = newRegion
        _errorMessage.value = null
    }

    private fun isValidEmail(email: String): Boolean {
        val emailLowerCase = email.trim().lowercase()
        return emailLowerCase.endsWith("@duoc.cl") ||
                emailLowerCase.endsWith("@profesor.duoc.cl") ||
                emailLowerCase.endsWith("@gmail.com")
    }

    // Función de registro
    fun register() {
        // Validaciones
        if (_name.value.isBlank()) {
            _errorMessage.value = "El nombre es obligatorio"
            return
        }

        if (_lastName.value.isBlank()) {
            _errorMessage.value = "El apellido es obligatorio"
            return
        }

        if (_email.value.isBlank()) {
            _errorMessage.value = "El correo es obligatorio"
            return
        }

        if (_email.value.length > 100) {
            _errorMessage.value = "El correo no puede tener más de 100 caracteres"
            return
        }

        if (!isValidEmail(_email.value)) {
            _errorMessage.value = "Formato de correo inválido"
            return
        }

        if (_password.value.isBlank()) {
            _errorMessage.value = "La contraseña es obligatoria"
            return
        }

        if (_password.value.length < 4 || _password.value.length > 10) {
            _errorMessage.value = "La contraseña debe tener entre 4 y 10 caracteres"
            return
        }

        if (_password.value != _confirmPassword.value) {
            _errorMessage.value = "Las contraseñas no coinciden"
            return
        }

        if (_phone.value.isBlank()) {
            _errorMessage.value = "El teléfono es obligatorio"
            return
        }

        if (_phone.value.length != 9) {
            _errorMessage.value = "El teléfono debe tener 9 dígitos"
            return
        }

        if (_address.value.isBlank()) {
            _errorMessage.value = "La dirección es obligatoria"
            return
        }

        if (_comuna.value.isBlank()) {
            _errorMessage.value = "La comuna es obligatoria"
            return
        }

        if (_region.value.isBlank()) {
            _errorMessage.value = "La región es obligatoria"
            return
        }

        // Si pasamos las validaciones
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Crear objeto Usuario
                val usuario = Usuario(
                    id = null,
                    nombre = _name.value.trim(),
                    apellido = _lastName.value.trim(),
                    email = _email.value.trim().lowercase(),
                    contrasenna = _password.value,
                    telefono = _phone.value.trim(),
                    direccion = _address.value.trim(),
                    comuna = _comuna.value.trim(),
                    region = _region.value.trim(),
                    rol = "Usuario"
                )

                // Crear instancia del Repository
                val userRepository = UsuarioRepository()

                // Llamar al repository
                val result = userRepository.registrarUsuario(usuario)

                when (result) {
                    is Resultado.Exito -> {
                        val usuarioRegistrado = result.datos

                        // Guardar usuario en CurrentUser
                        CurrentUser.save(usuarioRegistrado)

                        _isSuccess.value = true
                        clearForm()
                    }
                    is Resultado.Error -> {
                        _errorMessage.value = result.excepcion.message ?: "Error al registrar"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearForm() {
        _name.value = ""
        _lastName.value = ""
        _email.value = ""
        _password.value = ""
        _confirmPassword.value = ""
        _phone.value = ""
        _address.value = ""
        _comuna.value = ""
        _region.value = ""
        _errorMessage.value = null
        _isSuccess.value = false
    }
}