package com.example.fnx_huerto_hogar.ui.theme.viewModel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fnx_huerto_hogar.data.model.Product
import com.example.fnx_huerto_hogar.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleProductoViewModel(
    private val productRepository: ProductRepository,
    private val productId : String
): ViewModel() {
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> =_isLoading.asStateFlow()

    private val _message =MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    private val _addToCart = MutableStateFlow(false)
    val addToCart: StateFlow<Boolean> =_addToCart.asStateFlow()

    init {
        chargeProduct()
    }
    private fun chargeProduct(){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val productFound = productRepository.getProductById(productId)
                _product.value = productFound
                if (productFound == null){
                    _message.value = "Producto no encontrado"
                }
            }catch (e: Exception){
                _message.value = "Error al cargar el producto: ${e.message}"
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun raiseQuantity(){
        val actualProduct = _product.value
        if (actualProduct != null && _quantity.value < actualProduct.stock){
            _quantity.value += 1
        }
    }

    fun lowerQuantity(){
        if (quantity.value >1){
            _quantity.value -=1
        }
    }

    fun addToCart(){
        viewModelScope.launch {
            _addToCart.value = true
            try {
                val product = _product.value
                if (product != null){
                    val success = productRepository.reduceStock(productId, _quantity.value)
                    if (success){
                        _message.value = "${_quantity.value} ${product.name} agregado al carrito"
                    }else{
                        _message.value = "No hay stock suficente"
                    }
                }
            }finally {
                _addToCart.value = false
            }
        }
    }

    fun cleanMessage(){
        _message.value = ""
    }
}