package com.example.delitelligencefrontend.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delitelligencefrontend.domain.ProductsUseCase
import com.example.delitelligencefrontend.model.EmployeeCreate
import com.example.delitelligencefrontend.model.EmployeeUpdate
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.model.StandardWeight
import com.example.delitelligencefrontend.model.mapper.PostEmployeeMapper
import com.example.delitelligencefrontend.model.mapper.ProductMapperStruct
import com.example.delitelligencefrontend.model.mapper.ProductMutationMapper
import com.example.delitelligencefrontend.modeldto.product.ProductCreate
import com.example.delitelligencefrontend.modeldto.product.ProductUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _standardWeights = MutableStateFlow<List<StandardWeight>>(emptyList())
    val standardWeights: StateFlow<List<StandardWeight>> = _standardWeights.asStateFlow()

    init {
        fetchAllProducts()
        fetchStandardWeights()
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
                println("Create Product Response: $response")
            } catch (e: Exception) {
                println("Error Creating Product: ${e.message}")
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            try {
                val response = product.id?.let { productsUseCase.executeDelete(it) }
                println("Delete Product Response: $response")
            } catch (e: Exception) {
                println("Error Deleting Product: ${e.message}")
            }
        }
    }

    fun updateProduct(productToUpdate: ProductUpdate) {
        viewModelScope.launch {
            try {
                val inputDto = ProductMutationMapper.INSTANCE.toProductUpdateDto(productToUpdate)
                val response = productsUseCase.execute(inputDto)
                println("Update Product Response: $response")
            } catch (e: Exception) {
                println("Error Updating Product: ${e.message}")
            }
        }
    }

    fun fetchStandardWeights() {
        viewModelScope.launch {
            try {
                val weights = productsUseCase.executeGetStandardWeights()
                _standardWeights.value = weights
            } catch (e: Exception) {
                println("Error Fetching Standard Weights: ${e.message}")
            }
        }
    }
}