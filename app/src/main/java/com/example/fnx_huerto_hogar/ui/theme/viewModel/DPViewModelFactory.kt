package com.example.fnx_huerto_hogar.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fnx_huerto_hogar.data.repository.ProductRepository

class DPViewModelFactory (
    private val repository: ProductRepository,
    private val productId: String
): ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>):T{
        if (modelClass.isAssignableFrom(DetalleProductoViewModel::class.java)){
            return DetalleProductoViewModel(repository, productId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

}