package com.example.fnx_huerto_hogar.ui.theme.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fnx_huerto_hogar.data.CheckoutState
import com.example.fnx_huerto_hogar.data.DeliveryType
import com.example.fnx_huerto_hogar.data.PaymentMethod
import com.example.fnx_huerto_hogar.data.model.CartItem
import com.example.fnx_huerto_hogar.data.repository.UsuarioRepository
import com.example.fnx_huerto_hogar.data.repository.WeatherRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel : ViewModel() {

    private val _state = MutableStateFlow(CheckoutState())
    val state: StateFlow<CheckoutState> = _state.asStateFlow()

    var cartItems: List<CartItem> = emptyList()
    var totalPrice: Double = 0.0

    // Estado para mostrar/ocultar mapa
    private val _showMap = MutableStateFlow(false)
    val showMap: StateFlow<Boolean> = _showMap.asStateFlow()

    // Ubicación seleccionada en el mapa
    private val _pickupLocation = mutableStateOf<LatLng?>(null)
    val pickupLocation: LatLng? get() = _pickupLocation.value

    // Repositorio del clima
    private val weatherRepository = WeatherRepository()
    var showConfirmationCard by mutableStateOf(false)
        private set

    fun showCard() {
        showConfirmationCard = true
    }


    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val currentUser = UsuarioRepository.getCurrentUser()
            currentUser?.let { user ->
                _state.update { state ->
                    state.copy(
                        deliveryAddress = "${user.direccion}, ${user.comuna}, ${user.region}",
                        recipientName = "${user.nombre} ${user.apellido}",
                        recipientPhone = user.telefono ?: ""
                    )
                }
            }
        }
    }

    fun savePickupLocation(location: LatLng) {
        _pickupLocation.value = location
    }

    fun clearPickupLocation() {
        _pickupLocation.value = null
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

    fun setDeliveryType(type: DeliveryType) {
        _state.update { it.copy(deliveryType = type) }
    }

    fun setPaymentMethod(method: PaymentMethod) {
        _state.update { it.copy(paymentMethod = method) }
    }

    fun updateDeliveryAddress(address: String) {
        _state.update { it.copy(deliveryAddress = address) }
    }

    fun updateRecipientInfo(name: String, phone: String) {
        _state.update { it.copy(recipientName = name, recipientPhone = phone) }
    }

    fun updateDeliveryInstruction(instructions: String) {
        _state.update { it.copy(deliveryInstruction = instructions) }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = "") }
    }


    fun confirmOrder(
        onWeatherReady: () -> Unit,
    ) {
        if (!validateForm()) return

        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                // 1. Obtener ubicación seleccionada
                val location = pickupLocation
                if (location == null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Debes seleccionar una ubicación en el mapa"
                        )
                    }
                    return@launch
                }

                // 2. Obtener información del clima (REEMPLAZA TU_API_KEY con la real)
                val weather = weatherRepository.getWeather(
                    lat = location.latitude,
                    lon = location.longitude,
                    apiKey = "5385be87a14db7b371ea7d1bb19fc80d"  // ¡IMPORTANTE!
                )

                val description = weather.weather.firstOrNull()?.description ?: "Desconocido"
                val temp = weather.main.temp

                // 3. Actualizar estado
                _state.update {
                    it.copy(
                        weatherDescription = description,
                        weatherTemperature = temp,
                        isLoading = false,
                        isConfirmed = true
                    )
                }

                // 4. Mostrar información del clima
                onWeatherReady()

                // 5. Luego de que el usuario vea el clima, navegar
                // Esto se maneja en el diálogo que se cierra

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al confirmar pedido: ${e.message}"
                    )
                }
            }
        }
    }
}
