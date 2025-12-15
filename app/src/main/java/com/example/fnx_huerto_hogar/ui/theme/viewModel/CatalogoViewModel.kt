package com.example.fnx_huerto_hogar.ui.theme.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fnx_huerto_hogar.data.model.Product
import com.example.fnx_huerto_hogar.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel : ViewModel() {
    private val repository = ProductRepository()

    // Los StateFlow para el manejo de la UI
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadAllProducts()
    }

    fun loadAllProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _selectedCategory.value = null

            repository.getAllProducts()
                .onSuccess { _products.value = it }
                .onFailure { _products.value = emptyList() }

            _isLoading.value = false
        }
    }

    fun filterByCategory(category: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            _selectedCategory.value = category

            val result = if (category == null) {
                repository.getAllProducts()
            } else {
                repository.getProductsByCategory(category)
            }

            result.onSuccess { _products.value = it }
                .onFailure { _products.value = emptyList() }

            _isLoading.value = false
        }
    }

    // Categorías disponibles como lista de Strings
    val categories: List<String>
        get() = listOf("FRUTAS", "VERDURAS", "ORGANICOS")
}