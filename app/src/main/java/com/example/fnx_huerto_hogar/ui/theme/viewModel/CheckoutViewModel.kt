package com.example.fnx_huerto_hogar.ui.theme.viewModel

import android.provider.Contacts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fnx_huerto_hogar.data.CheckoutState
import com.example.fnx_huerto_hogar.data.DeliveryType
import com.example.fnx_huerto_hogar.data.PaymentMethod
import com.example.fnx_huerto_hogar.data.model.CartItem
import com.example.fnx_huerto_hogar.data.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel: ViewModel() {
    private val _state = MutableStateFlow(CheckoutState())
    val state: StateFlow<CheckoutState> = _state.asStateFlow()

    var cartItems: List<CartItem> = emptyList()
    var totalPrice: Double = 0.0

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val currentUser = UserRepository.getCurrentUser()
            currentUser?.let { user ->
                _state.update { state ->
                    state.copy(
                        deliveryAddress = "${user.address}, ${user.comuna}, ${user.region}",
                        recipientName = "${user.name} ${user.lastName}",
                        recipientPhone = user.phone
                    )
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        val currentState = _state.value
        if (currentState.deliveryType == DeliveryType.HOME_DELIVERY &&
            currentState.deliveryAddress.isBlank()
        ) {
            _state.update { it.copy(errorMessage = "Por favor ingresa una dirección de entrega") }
            return false
        }

        if (currentState.recipientName.isBlank() || currentState.recipientPhone.isBlank()) {
            _state.update { it.copy(errorMessage = "Por favor completa los datos del destinatario") }
            return false
        }

        _state.update { it.copy(errorMessage = "") }
        return true
    }


    fun setDeliveryType(type: DeliveryType){
        _state.update { it.copy(deliveryType = type) }
    }

    fun setPaymentMethod(method: PaymentMethod){
        _state.update { it.copy(paymentMethod = method) }
    }

    fun updateDeliveryAddress(address: String){
        _state.update { it.copy(deliveryAddress = address) }
    }

    fun updateRecipientInfo(name: String, phones: String){
        _state.update { it.copy(recipientName = name, recipientPhone = phones) }
    }

    fun updateDeliveryInstruction(instructions: String){
        _state.update { it.copy(deliveryInstruction = instructions) }
    }

    fun clearError(){
        _state.update {it.copy(errorMessage = "")}
    }

    fun confirmOrder(onSuccess: () -> Unit){
        if(!validateForm()) return
        _state.update {it.copy(isLoading = true)}

        viewModelScope.launch {
            delay(2000)
            _state.update { it.copy(isLoading = false, isConfirmed = true) }
            onSuccess()
        }
    }
}