package com.example.fnx_huerto_hogar.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fnx_huerto_hogar.data.ChileLocations
import com.example.fnx_huerto_hogar.data.model.User
import com.example.fnx_huerto_hogar.data.model.UserRole
import com.example.fnx_huerto_hogar.data.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val userRepository = UserRepository

    // Estados para los campos
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> =_email.asStateFlow()

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

    // Estados para los dropdowns
    private val _regions = MutableStateFlow<List<String>>(ChileLocations.regions)
    val regions: StateFlow<List<String>> = _regions.asStateFlow()

    private val _communes = MutableStateFlow<List<String>>(emptyList())
    val communes: StateFlow<List<String>> = _communes.asStateFlow()

    private val _showRegionDropdown = MutableStateFlow(false)
    val showRegionDropdown: StateFlow<Boolean> = _showRegionDropdown.asStateFlow()

    private val _showCommuneDropdown = MutableStateFlow(false)
    val showCommuneDropdown: StateFlow<Boolean> = _showCommuneDropdown.asStateFlow()

    // Funciones para actualizar campos
    fun onNameChange(newName: String){
        _name.value = newName
        _errorMessage.value = null
    }

    fun onLastNameChange(newLastName: String){
        _lastName.value = newLastName
        _errorMessage.value = null
    }

    fun onEmailChange(newEmail: String){
        _email.value = newEmail
        _errorMessage.value = null
    }

    fun onPasswordChange(newPassword: String){
        _password.value = newPassword
        _errorMessage.value = null
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
        _errorMessage.value = null
    }

    fun onPhoneChange(newPhone: String){
        _phone.value = newPhone
        _errorMessage.value = null
    }

    fun onAdressChange(newAdress: String){
        _address.value = newAdress
        _errorMessage.value = null
    }

    fun onComunaChange(newComuna: String) {
        _comuna.value = newComuna
        _errorMessage.value = null
        _showCommuneDropdown.value = false
    }

    fun onRegionChange(newRegion: String) {
        _region.value = newRegion
        _comuna.value = "" // Resetear comuna cuando cambia la región
        _communes.value = ChileLocations.communesByRegion[newRegion] ?: emptyList()
        _errorMessage.value = null
        _showRegionDropdown.value = false
    }

    // Funciones para controlar dropdowns
    fun onRegionDropdownToggle(expanded: Boolean) {
        _showRegionDropdown.value = expanded
    }

    fun onCommuneDropdownToggle(expanded: Boolean) {
        if (_region.value.isNotEmpty()) {
            _showCommuneDropdown.value = expanded
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailLowerCase = email.trim().lowercase()
        return emailLowerCase.endsWith("@duoc.cl") ||
                emailLowerCase.endsWith("@profesor.duoc.cl") ||
                emailLowerCase.endsWith("@gmail.com")
    }

    // Función de registro
    fun register() {
        // Validaciones...
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
                val user = User(
                    email = _email.value.trim().lowercase(),
                    name = _name.value.trim(),
                    lastName = _lastName.value.trim(),
                    password = _password.value,
                    phone = _phone.value,
                    address = _address.value.trim(),
                    comuna = _comuna.value.trim(),
                    region = _region.value.trim(),
                    rol = UserRole.USER
                )

                val success = userRepository.registerUser(user)
                if (success) {
                    _isSuccess.value = true
                    delay(1500)
                    clearForm()
                } else {
                    _errorMessage.value = "El correo ya está registrado"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al registrar el usuario: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearForm(){
        _name.value = ""
        _lastName.value = ""
        _email.value = ""
        _password.value = ""
        _confirmPassword.value = ""
        _phone.value = ""
        _address.value = ""
        _comuna.value = ""
        _region.value = ""
        _communes.value = emptyList()
        _errorMessage.value = null
        _isSuccess.value = false
        _showRegionDropdown.value = false
        _showCommuneDropdown.value = false
    }
}