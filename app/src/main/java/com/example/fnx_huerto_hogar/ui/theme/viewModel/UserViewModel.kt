package com.example.fnx_huerto_hogar.ui.theme.viewModel

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fnx_huerto_hogar.data.model.User
import com.example.fnx_huerto_hogar.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {
    private val _currentUser = mutableStateOf(UserRepository.getCurrentUser())
    val currentUser: State<User?> = _currentUser

    fun updateProfilePicture(uri: Uri){
        viewModelScope.launch {
            UserRepository.updateProfilePicture(uri.toString())
            _currentUser.value = UserRepository.getCurrentUser()
        }
    }

}