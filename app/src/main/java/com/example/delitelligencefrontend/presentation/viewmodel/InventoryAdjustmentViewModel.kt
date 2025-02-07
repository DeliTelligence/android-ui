/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video

https://chatgpt.com
prompt: build a view model for my kotilin screen using these use cases to get the data package com.example.delitelligencefrontend.domain

import com.example.delitelligence.type.DeliSaleInputDto
import com.example.delitelligence.type.InventoryAdjustmentInputDto

class PostAdjustmentUseCase(
    private val inventoryClient: InventoryClient
) {
    suspend fun execute(input: InventoryAdjustmentInputDto): String? {
        return inventoryClient.createAdjustment(input)
    }
} and package com.example.delitelligencefrontend.domain

import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.model.Supplier

class GetSupplierUseCase(
    private val supplierClient: SupplierClient
) {
    suspend fun execute(): List<Supplier> {
        return supplierClient.getAllSuppliers()
    }
} and    suspend fun execute(): List<Product> {
        return productClient.getAllProducts()
    }

*/

package com.example.delitelligencefrontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delitelligencefrontend.model.InventoryAdjustment
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.model.Supplier
import com.example.delitelligencefrontend.domain.ProductsUseCase
import com.example.delitelligencefrontend.domain.SupplierUseCase
import com.example.delitelligencefrontend.domain.InventoryUseCase
import com.example.delitelligencefrontend.enumformodel.AdjustmentType
import com.example.delitelligencefrontend.model.mapper.InventoryAdjustmentMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryAdjustmentViewModel @Inject constructor(
    private val postAdjustmentUseCase: InventoryUseCase,
    private val getSuppliersUseCase: SupplierUseCase,
    private val getProductsUseCase: ProductsUseCase
) : ViewModel() {

    private val _supplierName = MutableStateFlow("")
    private val _productName = MutableStateFlow("")
    private val _orderWeight = MutableStateFlow(0.0)
    private val _costPerBox = MutableStateFlow(0.0)
    private val _adjustmentType = MutableStateFlow(AdjustmentType.DELIVERY) // Set default to DELIVERY
    private val _reason = MutableStateFlow<String?>(null) // Set default to null

    private val _suppliers = MutableStateFlow<List<Supplier>>(emptyList())
    private val _products = MutableStateFlow<List<Product>>(emptyList())

    val supplierName: StateFlow<String> = _supplierName
    val productName: StateFlow<String> = _productName
    val orderWeight: StateFlow<Double> = _orderWeight
    val costPerBox: StateFlow<Double> = _costPerBox
    val adjustmentType: StateFlow<AdjustmentType> = _adjustmentType
    val suppliers: StateFlow<List<Supplier>> = _suppliers
    val products: StateFlow<List<Product>> = _products
    val reason: StateFlow<String?> = _reason

    init {
        viewModelScope.launch {
            _suppliers.value = getSuppliersUseCase.execute()
            _products.value = getProductsUseCase.execute()
        }
    }

    fun updateSupplierName(name: String) {
        _supplierName.value = name
    }

    fun updateProductName(name: String) {
        _productName.value = name
        // Automatically update cost per box if it's a waste adjustment
        if (_adjustmentType.value == AdjustmentType.WASTE) {
            updateCostForWaste()
        }
    }

    fun updateOrderWeight(weight: Double) {
        _orderWeight.value = weight
        // Automatically update cost per box if it's a waste adjustment
        if (_adjustmentType.value == AdjustmentType.WASTE) {
            updateCostForWaste()
        }
    }

    fun updateCostPerBox(cost: Double) {
        _costPerBox.value = cost
    }

    fun updateAdjustmentType(type: AdjustmentType) {
        _adjustmentType.value = type
        if (type != AdjustmentType.WASTE) {
            _reason.value = null // Clear reason if type is not WASTE
        }
    }

    fun updateReason(reason: String) {
        _reason.value = reason
    }

    private fun updateCostForWaste() {
        val selectedProduct = _products.value.find { it.productName == _productName.value }
        selectedProduct?.let {
            _costPerBox.value = (it.productPrice ?: 0.0) * _orderWeight.value
        }
    }

    fun submitAdjustment() {
        val adjustment = InventoryAdjustment(
            supplierName = _supplierName.value,
            productName = _productName.value,
            orderWeight = _orderWeight.value,
            costPerBox = _costPerBox.value,
            adjustmentType = _adjustmentType.value,
            reason = _reason.value ?: "" // Provide a default value if reason is null
        )

        val adjustmentDto = InventoryAdjustmentMapper.INSTANCE.toInventoryAdjustmentInputDto(adjustment)
        viewModelScope.launch {
            postAdjustmentUseCase.execute(adjustmentDto)
            // Clear fields on successful submission
            clearFields()
        }
    }

    private fun clearFields() {
        _supplierName.value = ""
        _productName.value = ""
        _orderWeight.value = 0.0
        _costPerBox.value = 0.0
        _adjustmentType.value = AdjustmentType.DELIVERY
        _reason.value = null
    }
}