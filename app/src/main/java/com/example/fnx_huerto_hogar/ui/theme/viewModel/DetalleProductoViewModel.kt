package com.example.fnx_huerto_hogar.ui.theme.viewModel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fnx_huerto_hogar.data.model.Product
import com.example.fnx_huerto_hogar.data.repository.CartRepository
import com.example.fnx_huerto_hogar.data.repository.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleProductoViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val productId: String
) : ViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    private val _addingToCart = MutableStateFlow(false)
    val addingToCart: StateFlow<Boolean> = _addingToCart.asStateFlow()

    init {
        chargeProduct()
    }

    private fun chargeProduct() {
        viewModelScope.launch {
            _isLoading.value = true

            productRepository.getProductById(productId)
                .onSuccess { product ->
                    _product.value = product
                }
                .onFailure { error ->
                    _message.value = "Error al cargar el producto: ${error.message}"
                }

            _isLoading.value = false
        }
    }

    fun raiseQuantity() {
        val actualProduct = _product.value
        if (actualProduct != null && _quantity.value < actualProduct.stock) {
            _quantity.value += 1
        }
    }

    fun lowerQuantity() {
        if (_quantity.value > 1) {
            _quantity.value -= 1
        }
    }

    fun addToCart(usuarioId: Long) {
        viewModelScope.launch {
            _addingToCart.value = true

            val currentProduct = _product.value
            if (currentProduct != null) {
                cartRepository.addToCart(usuarioId, currentProduct.id, _quantity.value)
                    .onSuccess {
                        _message.value = "Producto agregado al carrito"
                        delay(2000)
                        _message.value = ""
                    }
                    .onFailure { error ->
                        _message.value = "Error al agregar al carrito: ${error.message}"
                    }
            } else {
                _message.value = "Error: Producto no disponible"
            }

            _addingToCart.value = false
        }
    }

    fun cleanMessage() {
        _message.value = ""
    }
}