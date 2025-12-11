package com.example.fnx_huerto_hogar.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.fnx_huerto_hogar.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel: ViewModel() {

    private val userRepository = UsuarioRepository

    //Estados de operación del viewModel
    private val _newEmail = MutableStateFlow("")
    val newEmail: StateFlow<String> = _newEmail.asStateFlow()

    private val _emailPassword = MutableStateFlow("")
    val emailPassword: StateFlow<String> = _emailPassword.asStateFlow()

    private val _currentPassword = MutableStateFlow("")
    val currentPassword: StateFlow<String> = _currentPassword.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword.asStateFlow()

    private val _confirmNewPassword = MutableStateFlow("")
    val confirmNewPassword: StateFlow<String> = _confirmNewPassword.asStateFlow()


    //Estados generales
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()


    //Funciones de actualización de estados
    fun onNewEmailChange(email: String){
        _newEmail.value = email
        clearMessages()
    }

    fun onEmailPasswordChange(password: String){
        _emailPassword.value = password
        clearMessages()
    }

    fun onCurrentPasswordChange(password: String){
        _currentPassword.value = password
        clearMessages()
    }

    fun onNewPasswordChange(password: String){
        _newPassword.value = password
        clearMessages()
    }

    fun onConfirmNewPasswordChange(password: String){
        _confirmNewPassword.value = password
        clearMessages()
    }


    //Funciones
    fun changeEmail(){
        val currentUser = userRepository.getCurrentUser()
        if (currentUser == null){
            _errorMessage.value = "No hay usuario loggeado"
            return
        }

        if (_newEmail.value.isBlank()){
            _errorMessage.value = "El nuevo email es obligatorio"
            return
        }

        if (!isValidEmail(_newEmail.value)){
            _errorMessage.value = "Formato de email inválido"
            return
        }

        if (_emailPassword.value.isBlank()){
            _errorMessage.value = "La contraseña es obligatoria"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _successMessage.value = null

            try {
                val success = userRepository.updateUserEmail(
                    oldEmail = currentUser.email,
                    newEmail = _newEmail.value.trim().lowercase(),
                    password = _emailPassword.value
                )

                if (success){
                    _successMessage.value = "Email actualizado correctamente"
                    clearEmailForm()
                }else {
                    _errorMessage.value = "Error al actualizar email"
                }
            }catch (e: Exception){
                _errorMessage.value = "Error: ${e.message}"
            }finally {
                _isLoading.value = false
            }
        }
    }

    //Función para cambiar contraseña
    fun changePassword(){
        val currentUser = userRepository.getCurrentUser()
        if (currentUser == null){
            _errorMessage.value = "No hay usuario loggeado"
            return
        }

        if (_currentPassword.value.isBlank()){
            _errorMessage.value = "Ingrese contraseña"
            return
        }

        if (_newPassword.value.isBlank()){
            _errorMessage.value = "Ingrese nueva contraseña"
            return
        }

        if (_newPassword.value.length < 4 || _newPassword.value.length > 10){
            _errorMessage.value = "La nueva contraseña entre 4 y 10 carácteres"
            return
        }

        if (_newPassword.value != _confirmNewPassword.value){
            _errorMessage.value = "Las contraseñas no coinciden"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _successMessage.value = null

            try {
                val success = userRepository.updateUserPassword(
                    email = currentUser.email,
                    currentPassword = _currentPassword.value,
                    newPassword = _newPassword.value
                )

                if (success){
                    _successMessage.value = "Contraseña actualizada correctamente"
                    clearPasswordForm()
                }else {
                    _errorMessage.value = "Error al actualizar la contraseña"
                }
            }catch (e: Exception){
                _errorMessage.value = "Error: ${e.message}"
            }finally {
                _isLoading.value = false
            }
        }
    }

    private fun isValidEmail(email: String): Boolean{
        val emailLowerCase = email.trim().lowercase()
        return emailLowerCase.endsWith("@duoc.cl") ||
                emailLowerCase.endsWith("@profesor.duoc.cl") ||
                emailLowerCase.endsWith("@gmail.com")
    }

    private fun clearEmailForm(){
        _newEmail.value = ""
        _emailPassword.value = ""
    }

    private fun clearPasswordForm(){
        _currentPassword.value = ""
        _newPassword.value = ""
        _confirmNewPassword.value = ""
    }

    private fun clearMessages(){
        _errorMessage.value = null
        _successMessage.value = null
    }

    fun getCurrentUser() = userRepository.getCurrentUser()

}