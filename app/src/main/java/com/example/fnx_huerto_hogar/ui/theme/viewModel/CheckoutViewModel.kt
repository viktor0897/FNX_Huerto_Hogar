package com.example.fnx_huerto_hogar.ui.theme.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fnx_huerto_hogar.data.CheckoutState
import com.example.fnx_huerto_hogar.data.DeliveryType
import com.example.fnx_huerto_hogar.data.PaymentMethod
import com.example.fnx_huerto_hogar.data.repository.CartRepository
import com.example.fnx_huerto_hogar.data.repository.UsuarioRepository
import com.example.fnx_huerto_hogar.data.repository.WeatherRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel : ViewModel() {

    private val cartRepository = CartRepository()
    private val weatherRepository = WeatherRepository()

    private val _state = MutableStateFlow(CheckoutState())
    val state: StateFlow<CheckoutState> = _state.asStateFlow()

    // Estado del carrito
    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    private val _totalItems = MutableStateFlow(0)
    val totalItems: StateFlow<Int> = _totalItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado para mostrar/ocultar mapa
    private val _showMap = MutableStateFlow(false)
    val showMap: StateFlow<Boolean> = _showMap.asStateFlow()

    // Ubicación seleccionada en el mapa
    private val _pickupLocation = mutableStateOf<LatLng?>(null)
    val pickupLocation: LatLng? get() = _pickupLocation.value

    // Confirmación de pedido
    var showConfirmationCard by mutableStateOf(false)
        private set

    init {
        loadUserData()
        loadCartData()
    }

    /**
     * Cargar datos del usuario actual desde UsuarioRepository.CurrentUser
     */
    private fun loadUserData() {
        val currentUser = UsuarioRepository.CurrentUser.get()
        currentUser?.let { user ->
            _state.update { state ->
                state.copy(
                    deliveryAddress = buildString {
                        if (!user.direccion.isNullOrBlank()) append(user.direccion)
                        if (!user.comuna.isNullOrBlank()) {
                            if (isNotEmpty()) append(", ")
                            append(user.comuna)
                        }
                        if (!user.region.isNullOrBlank()) {
                            if (isNotEmpty()) append(", ")
                            append(user.region)
                        }
                    },
                    recipientName = "${user.nombre} ${user.apellido}",
                    recipientPhone = user.telefono ?: ""
                )
            }
        }
    }

    /**
     * Cargar datos del carrito del usuario actual
     */
    private fun loadCartData() {
        viewModelScope.launch {
            _isLoading.value = true

            val currentUser = UsuarioRepository.CurrentUser.get()
            val userId = currentUser?.id

            // ✅ Validar que existe usuario Y que tiene ID
            if (userId == null) {
                _state.update { it.copy(errorMessage = "Debes iniciar sesión para continuar") }
                _isLoading.value = false
                return@launch
            }

            // Ahora userId es Long (no nullable)
            val result = cartRepository.getCart(userId)
            result.onSuccess { cart ->
                _totalPrice.value = cart.total
                _totalItems.value = cart.items.sumOf { it.quantity }
            }.onFailure { error ->
                _state.update { it.copy(errorMessage = error.message ?: "Error al cargar carrito") }
            }

            _isLoading.value = false
        }
    }

    /**
     * Recargar el carrito manualmente
     */
    fun refreshCart() {
        loadCartData()
    }

    fun savePickupLocation(location: LatLng) {
        _pickupLocation.value = location
    }

    fun clearPickupLocation() {
        _pickupLocation.value = null
    }

    fun toggleMapVisibility() {
        _showMap.value = !_showMap.value
    }

    fun showMap() {
        _showMap.value = true
    }

    fun hideMap() {
        _showMap.value = false
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

        if (_pickupLocation.value == null) {
            _state.update { it.copy(errorMessage = "Debes seleccionar una ubicación en el mapa") }
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

    fun showCard() {
        showConfirmationCard = true
    }

    fun hideCard() {
        showConfirmationCard = false
    }

    /**
     * Confirmar el pedido con validación y obtención del clima
     */
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

                // 2. Obtener información del clima
                val weather = weatherRepository.getWeather(
                    lat = location.latitude,
                    lon = location.longitude,
                    apiKey = "5385be87a14db7b371ea7d1bb19fc80d"
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

    /**
     * Crear la orden en el backend
     */
    suspend fun createOrder(): Result<Long> {
        return try {
            val currentUser = UsuarioRepository.CurrentUser.get()
            val userId = currentUser?.id

            // ✅ Validar que existe usuario Y que tiene ID
            if (userId == null) {
                return Result.failure(Exception("Debes iniciar sesión para continuar"))
            }

            // TODO Implementar llamada real al backend para crear orden
            // val orderResponse = orderRepository.createOrder(userId, ...)

            // Por ahora retornamos un ID de ejemplo
            Result.success(1L)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}