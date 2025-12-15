package com.example.fnx_huerto_hogar.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fnx_huerto_hogar.data.model.Cart
import com.example.fnx_huerto_hogar.data.model.CartItem
import com.example.fnx_huerto_hogar.data.model.Product
import com.example.fnx_huerto_hogar.data.repository.CartRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val cartRepository = CartRepository()

    private val _cart = MutableStateFlow<Cart?>(null)
    val cart: StateFlow<Cart?> = _cart.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _showSuccessMessage = MutableStateFlow(false)
    val showSuccessMessage: StateFlow<Boolean> = _showSuccessMessage.asStateFlow()

    fun loadCart(usuarioId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            cartRepository.getCart(usuarioId)
                .onSuccess { _cart.value = it }
                .onFailure { _errorMessage.value = it.message }

            _isLoading.value = false
        }
    }

    fun addToCart(usuarioId: Long, productoId: String, cantidad: Int = 1) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            cartRepository.addToCart(usuarioId, productoId, cantidad)
                .onSuccess {
                    _cart.value = it
                    _showSuccessMessage.value = true
                    delay(2000)
                    _showSuccessMessage.value = false
                }
                .onFailure { _errorMessage.value = it.message }

            _isLoading.value = false
        }
    }

    // CORREGIDO: itemId: Long → productoId: String
    fun updateQuantity(usuarioId: Long, productoId: String, cantidad: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            cartRepository.updateCartItem(usuarioId, productoId, cantidad)
                .onSuccess { _cart.value = it }
                .onFailure { _errorMessage.value = it.message }

            _isLoading.value = false
        }
    }

    // CORREGIDO: itemId: Long → productoId: String
    fun removeItem(usuarioId: Long, productoId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            cartRepository.removeFromCart(usuarioId, productoId)
                .onSuccess { _cart.value = it }
                .onFailure { _errorMessage.value = it.message }

            _isLoading.value = false
        }
    }

    fun clearCart(usuarioId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            cartRepository.clearCart(usuarioId)
                .onSuccess {
                    _cart.value = null
                    loadCart(usuarioId)
                }
                .onFailure { _errorMessage.value = it.message }

            _isLoading.value = false
        }
    }
}