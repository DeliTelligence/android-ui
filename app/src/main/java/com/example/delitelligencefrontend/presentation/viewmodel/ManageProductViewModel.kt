package com.example.delitelligencefrontend.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delitelligencefrontend.domain.ProductsUseCase
import com.example.delitelligencefrontend.model.EmployeeCreate
import com.example.delitelligencefrontend.model.EmployeeUpdate
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.model.mapper.PostEmployeeMapper
import com.example.delitelligencefrontend.model.mapper.ProductMapperStruct
import com.example.delitelligencefrontend.model.mapper.ProductMutationMapper
import com.example.delitelligencefrontend.modeldto.product.ProductCreate
import com.example.delitelligencefrontend.modeldto.product.ProductUpdate
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

    fun createProduct(productToCreate: ProductCreate) {
        viewModelScope.launch {
            try {
                val inputDto = ProductMutationMapper.INSTANCE.toProductCreateDto(productToCreate)
                val response = productsUseCase.execute(inputDto)
                // Handle response (e.g., update UI, show a success message, etc.)
                println("Create Product Response: $response")
            } catch (e: Exception) {
                // Handle error (e.g., show an error message)
                println("Error Product Employee: ${e.message}")
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

    fun updateProduct(productToUpdate: ProductUpdate) {
        viewModelScope.launch {
            try {
                val inputDto = ProductMutationMapper.INSTANCE.toProductUpdateDto(productToUpdate)
                val response = productsUseCase.execute(inputDto)
                // Handle response (e.g., update UI, show a success message, etc.)
                println("Update Product Response: $response")
            } catch (e: Exception) {
                // Handle error (e.g., show an error message)
                println("Error Updating Product: ${e.message}")
            }
        }
    }
}