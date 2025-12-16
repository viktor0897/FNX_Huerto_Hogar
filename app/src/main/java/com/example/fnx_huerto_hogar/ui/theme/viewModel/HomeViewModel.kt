package com.example.fnx_huerto_hogar.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fnx_huerto_hogar.data.model.Product
import com.example.fnx_huerto_hogar.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val productos: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun cargarProductosDestacados() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // El repository devuelve Result<List<Product>>
            val result = repository.getAllProducts()

            result.onSuccess { productos ->
                _uiState.update {
                    it.copy(
                        productos = productos.take(5), // Solo los primeros 5
                        isLoading = false,
                        error = null
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        productos = emptyList(),
                        isLoading = false,
                        error = exception.message ?: "Error al cargar productos"
                    )
                }
            }
        }
    }
}