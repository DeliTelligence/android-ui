package com.example.delitelligencefrontend.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delitelligencefrontend.domain.ProductsUseCase
import com.example.delitelligencefrontend.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageProductViewModel @Inject constructor(
    private val productsUseCase: ProductsUseCase
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        fetchAllProducts()
    }

    fun fetchAllProducts() {
        viewModelScope.launch {
            _products.value = productsUseCase.execute()
        }
    }

    fun searchProducts(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _products.value = if (query.isEmpty()) {
                productsUseCase.execute()
            } else {
                productsUseCase.execute().filter {
                    it.productName?.contains(query, ignoreCase = true) ?: false
                }
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            // Handle product deletion and refresh the list
            // Actual deletion logic should be implemented here
            fetchAllProducts()
        }
    }

    fun editProduct(product: Product) {
        viewModelScope.launch {
            // Handle product editing
        }
    }
}