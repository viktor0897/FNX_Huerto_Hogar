package com.example.fnx_huerto_hogar.ui.theme.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fnx_huerto_hogar.data.model.Product
import com.example.fnx_huerto_hogar.data.model.ProductCategory
import com.example.fnx_huerto_hogar.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel : ViewModel(){
    private val repository = ProductRepository()

    //Los stateFlow para el manejo de la UI
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()
    private val _selectedCategory = MutableStateFlow<ProductCategory?>(null)
    val selectedCategory: StateFlow<ProductCategory?> = _selectedCategory.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadAllProducts()
    }

    fun loadAllProducts(){
        viewModelScope.launch {
            _isLoading.value = true
            _selectedCategory.value = null

            try {
                _products.value = repository.getAllProducts()
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun filterByCategory(category: ProductCategory?){
        viewModelScope.launch {
            _isLoading.value = true
            _selectedCategory.value = category

            try {
                _products.value = if (category == null){
                    repository.getAllProducts()
                }else{
                    repository.getProductsByCategory(category)
                }
            }finally {
                _isLoading.value = false
            }
        }
    }

    val categories: List<ProductCategory>
        get() = ProductCategory.entries.toList()

}