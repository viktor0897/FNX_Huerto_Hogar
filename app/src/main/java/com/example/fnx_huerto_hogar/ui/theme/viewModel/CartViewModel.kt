package com.example.fnx_huerto_hogar.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fnx_huerto_hogar.data.model.CartItem
import com.example.fnx_huerto_hogar.data.model.Product
import com.example.fnx_huerto_hogar.data.repository.CartRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel: ViewModel(){

    private val repository = CartRepository

    //Estados
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalItems = MutableStateFlow(0)
    val totalItems: StateFlow<Int> = _totalItems.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    //Init
    init {
        observeCart()
        observeTotals()
    }

    private fun observeCart() {
        viewModelScope.launch {
            repository.getCart().collect { items ->
                _cartItems.value = items
            }
        }
    }


    private fun observeTotals() {
        viewModelScope.launch {
            repository.getTotalItems().collect { total ->
                _totalItems.value = total
            }
        }

        viewModelScope.launch {
            repository.calculateCartTotal().collect { total ->
                _totalPrice.value = total
            }
        }
    }

//    //Agregar al carrito
//    fun addToCart(productId: String, name: String, price: Double, image: Int, quantity: Int){
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                val success = repository.addToCart(productId, name, price, image, quantity)
//                if (success){
//                    _message.value = "Producto Agregado"
//                } else {
//                    _message.value = "Error al agregar producto"
//                }
//            }finally{
//                _isLoading.value = false
//            }
//
//            //Limpiar mensaje después de x tiempo
//            launch {
//                delay(2000)
//                _message.value = ""
//            }
//        }
//    }

    //Actualizar cantidad
    fun updateQuantity(productId: String, newQuantity: Int){
        viewModelScope.launch {
            repository.updateQuantity(productId, newQuantity)
        }
    }

    //deletear
    fun removeFromCart(productId: String){
        viewModelScope.launch {
            repository.deleteFromCart(productId)
            _message.value = "Producto eliminado"

            launch {
                delay(2000)
                _message.value = ""
            }
        }
    }

    //Vaciar Carrito
    fun clearCart(){
        viewModelScope.launch {
            repository.emptyCart()
            _message.value = "Carrito vaciado"

            launch {
                delay(2000)
                _message.value = ""
            }
        }
    }

    //Incrementar cantidad en el carro
    fun incrementQuantity(productId: String){
        viewModelScope.launch {
            val currentItem = _cartItems.value.find { it.productId == productId }
            currentItem?.let {item ->
                updateQuantity(productId, item.quantity +1)
            }
        }
    }

    //Decrementar cantidad en el carro
    fun decrementQuantity(productId: String) {
        viewModelScope.launch {
            val currentItem = _cartItems.value.find { it.productId == productId }
            currentItem?.let { item ->
                if (item.quantity > 1) {
                    updateQuantity(productId, item.quantity - 1)
                } else {
                    removeFromCart(productId)
                }
            }
        }
    }

    //limpiar mensaje
    fun clearMessage(){
        _message.value = ""
    }
}