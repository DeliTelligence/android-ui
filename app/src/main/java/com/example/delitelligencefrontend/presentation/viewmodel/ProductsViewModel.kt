/*https://chatgpt.com
prompt: 'How to build a view model in android, Im using jetpack compose, Dagger Hilt and im using this Kotlin Interface, I have a graphql enpoint and a REST python endpoint
as shown below with these methods can you build a view model for the screen
package com.example.delitelligencefrontend.domain

import com.example.delitelligencefrontend.model.StatusResponse
import com.example.delitelligencefrontend.model.WeightResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface WeightApiService {
    @POST("connect")
    suspend fun connectToScale(): Response<StatusResponse>

    @POST("enable_notify")
    suspend fun enableNotifications(): Response<StatusResponse>

    @POST("disable_notify")
    suspend fun disableNotifications(): Response<StatusResponse>

    @GET("weight")
    suspend fun getWeightData(): Response<WeightResponse>

    @POST("tare")
    suspend fun tareScale(): Response<StatusResponse>

    @POST("disconnect")
    suspend fun disconnectFromScale(): Response<StatusResponse>
} and package com.example.delitelligencefrontend.domain

import com.example.delitelligence.type.ProductType
import com.example.delitelligencefrontend.model.Employee
import com.example.delitelligencefrontend.model.Product

class GetProductsUseCase(
    private val productClient: ProductClient

) {
    suspend fun execute(productId: String): Product? {
        return productClient.getProductsById(productId)
    }
    suspend fun execute(productType: ProductType): List<Product> {
        return productClient.getProductsByType(productType)
    }

    suspend fun execute(): List<Product> {
        return productClient.getAllProducts()
    }
}*/

package com.example.delitelligencefrontend.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delitelligencefrontend.domain.GetProductsUseCase
import com.example.delitelligencefrontend.domain.PostDeliSaleUseCase
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.model.WeightResponse
import com.example.delitelligencefrontend.model.StatusResponse
import com.example.delitelligencefrontend.domain.WeightApiService
import com.example.delitelligencefrontend.enumformodel.PortionType
import com.example.delitelligencefrontend.enumformodel.SaleType
import com.example.delitelligencefrontend.enumformodel.StandardType
import com.example.delitelligencefrontend.model.DeliProduct
import com.example.delitelligencefrontend.model.DeliSale
import com.example.delitelligencefrontend.model.mapper.DeliSaleMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val weightApiService: WeightApiService,
    private val postDeliSaleUseCase: PostDeliSaleUseCase
) : ViewModel() {

    private val _hotFoodProductsFilling = MutableStateFlow<List<Product>>(emptyList())
    val hotFoodProductsFilling: StateFlow<List<Product>> = _hotFoodProductsFilling

    private val _breakfastProducts = MutableStateFlow<List<Product>>(emptyList())
    val breakfastProducts: StateFlow<List<Product>> = _breakfastProducts

    // Product-related state flows
    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    val allProducts: StateFlow<List<Product>> = _allProducts

    private val _coldFoodProducts = MutableStateFlow<List<Product>>(emptyList())
    val coldFoodProducts: StateFlow<List<Product>> = _coldFoodProducts

    private val _hotFoodProducts = MutableStateFlow<List<Product>>(emptyList())
    val hotFoodProducts: StateFlow<List<Product>> = _hotFoodProducts

    private val _mainFillingProducts = MutableStateFlow<List<Product>>(emptyList())
    val mainFillingProducts: StateFlow<List<Product>> = _mainFillingProducts

    private val _fillingProducts = MutableStateFlow<List<Product>>(emptyList())
    val fillingProducts: StateFlow<List<Product>> = _fillingProducts

    // Scale-related state flows
    private val _displayedValue = MutableStateFlow<Pair<Double, Boolean>>(Pair(0.0, false))
    val displayedValue: StateFlow<Pair<Double, Boolean>> = _displayedValue
    // The Pair contains (value, isDifference)

    private val _scaleStatus = MutableStateFlow("")
    val scaleStatus: StateFlow<String> = _scaleStatus

    private val _isScaleConnected = MutableStateFlow(false)
    val isScaleConnected: StateFlow<Boolean> = _isScaleConnected

    private val _isWeighing = MutableStateFlow(false)
    val isWeighing: StateFlow<Boolean> = _isWeighing

    private val _areNotificationsEnabled = MutableStateFlow(false)
    val areNotificationsEnabled: StateFlow<Boolean> = _areNotificationsEnabled

    private val _isWeightErrorSignificant = MutableStateFlow(false)
    val isWeightErrorSignificant: StateFlow<Boolean> = _isWeightErrorSignificant

    private val _currentDeliSale = MutableStateFlow<DeliSale?>(null)
    val currentDeliSale: StateFlow<DeliSale?> = _currentDeliSale

    init {
        fetchAllProducts()
        connectToScaleAndEnableNotifications()
    }

     fun fetchAllProducts() {
        viewModelScope.launch {
            try {
                val fetchedProducts = getProductsUseCase.execute()
                _allProducts.value = fetchedProducts
                _coldFoodProducts.value = fetchedProducts.filter { it.productType?.equals("MADE_FOOD_COLD", ignoreCase = true) == true }
                _hotFoodProducts.value = fetchedProducts.filter { it.productType?.equals("MADE_FOOD_HOT", ignoreCase = true) == true }
                _mainFillingProducts.value = fetchedProducts.filter { it.productType?.equals("MAIN_FILLING_FOOD", ignoreCase = true) == true }
                _fillingProducts.value = fetchedProducts.filter { it.productType?.equals("COLD_FOOD", ignoreCase = true) == true }
                _hotFoodProductsFilling.value = fetchedProducts.filter { it.productType?.equals("HOT_FOOD", ignoreCase = true) == true }
                _breakfastProducts.value = fetchedProducts.filter { it.productType?.equals("BREAKFAST_FOOD", ignoreCase = true) == true }


            } catch (e: Exception) {
                _scaleStatus.value = "Error fetching products: ${e.message}"
            }
        }
    }

    private fun connectToScaleAndEnableNotifications() {
        viewModelScope.launch {
            try {
                val connectResponse = weightApiService.connectToScale()
                if (connectResponse.isSuccessful) {
                    _isScaleConnected.value = true
                    _scaleStatus.value = "Connected to scale"

                    val notifyResponse = weightApiService.enableNotifications()
                    if (notifyResponse.isSuccessful) {
                        _areNotificationsEnabled.value = true
                        _scaleStatus.value = "Connected and notifications enabled"
                    } else {
                        _scaleStatus.value = "Connected, but failed to enable notifications"
                    }
                } else {
                    _scaleStatus.value = "Failed to connect to scale"
                }
            } catch (e: Exception) {
                _scaleStatus.value = "Error connecting to scale: ${e.message}"
            }
        }
    }


    fun fetchWeightData(deliProduct: DeliProduct) {
        if (_isScaleConnected.value && _areNotificationsEnabled.value) {
            _isWeighing.value = true
            viewModelScope.launch {
                try {
                    val response = weightApiService.getWeightData()
                    if (response.isSuccessful) {
                        response.body()?.let { weightResponse ->
                            val actualWeight = weightResponse.weight.toDouble()
                            _displayedValue.value = Pair(actualWeight, false)

                            // Calculate expected weight based on portionType
                            val expectedWeight = when (deliProduct.portionType) {
                                PortionType.SALAD, PortionType.FILLING -> {
                                    deliProduct.products.sumOf { product ->
                                        product.standardWeightProducts?.sumOf {
                                            if (deliProduct.portionType == PortionType.SALAD && it.standardWeight?.standardType == StandardType.SALAD.name
                                                || deliProduct.portionType == PortionType.FILLING && it.standardWeight?.standardType == StandardType.FILLING.name) {
                                                it.standardWeightValue?.toDouble() ?: 0.0
                                            } else 0.0
                                        } ?: 0.0
                                    }
                                }
                                PortionType.QUANTITY -> 0.0
                            }

                            // Calculate weight error
                            val error = actualWeight - expectedWeight

                            // Check if error is significant (more than 10%)
                            val isSignificantError = expectedWeight != 0.0 && abs(error / (expectedWeight)) > 0.1
                            _isWeightErrorSignificant.value = isSignificantError

                            // After 1 second, show the difference
                            delay(1000)
                            _displayedValue.value = Pair(error, true)

                            // Update the currentDeliSale with the weight data and calculated values
                            _currentDeliSale.value = _currentDeliSale.value?.copy(
                                saleWeight = actualWeight,
                                differenceWeight = error,
                                wastePerSaleValue = error * deliProduct.calculateTotalPrice()
                            )
                        }
                    } else {
                        _scaleStatus.value = "Error fetching weight data"
                    }
                } catch (e: Exception) {
                    _scaleStatus.value = "Error fetching weight data: ${e.message}"
                } finally {
                    _isWeighing.value = false
                }
            }
        } else {
            _scaleStatus.value = "Scale not connected or notifications not enabled"
        }
    }
    fun tareScale() {
        if (_isScaleConnected.value) {
            viewModelScope.launch {
                try {
                    val response = weightApiService.tareScale()
                    if (response.isSuccessful) {
                        _displayedValue.value = Pair(0.0, false)
                        _scaleStatus.value = "Scale tared"
                    } else {
                        _scaleStatus.value = "Failed to tare scale"
                    }
                } catch (e: Exception) {
                    _scaleStatus.value = "Error taring scale: ${e.message}"
                }
            }
        } else {
            _scaleStatus.value = "Scale not connected"
        }
    }

    fun disableNotificationsOnly() {
        if (_isScaleConnected.value && _areNotificationsEnabled.value) {
            viewModelScope.launch {
                try {
                    val response = weightApiService.disableNotifications()
                    if (response.isSuccessful) {
                        _areNotificationsEnabled.value = false
                        _scaleStatus.value = "Notifications disabled"
                    } else {
                        _scaleStatus.value = "Failed to disable notifications"
                    }
                } catch (e: Exception) {
                    _scaleStatus.value = "Error disabling notifications: ${e.message}"
                }
            }
        }
    }

    fun setCurrentDeliSale(deliSale: DeliSale) {
        _currentDeliSale.value = deliSale
        Log.d("ViewModel", "Set Current DeliSale: $deliSale")
    }

    override fun onCleared() {
        super.onCleared()
        disableNotificationsOnly()
    }

    fun postDeliSale(deliSale: DeliSale) {
        viewModelScope.launch {
            try {
                val inputDto = DeliSaleMapper.INSTANCE.toDeliSaleInputDto(deliSale)
                val response = postDeliSaleUseCase.execute(inputDto)
                if (response != null) {
                    _scaleStatus.value = "Sale posted successfully: $response"
                } else {
                    _scaleStatus.value = "Failed to post sale"
                }
            } catch (e: Exception) {
                _scaleStatus.value = "Error posting sale: ${e.message}"
            }
        }
    }
    fun updateCurrentDeliSale(currentDeliSale: DeliSale): DeliSale {
        Log.d("ViewModel", "Updating Current DeliSale: $currentDeliSale")
        var totalPrice = 0.0

        // Iterate over the products and sum their prices with null assertion
        for (product in currentDeliSale.deliProduct.products) {
            totalPrice += product.productPrice!! // Null assertion
        }

        // Add deliProduct's price
        totalPrice += currentDeliSale.deliProduct.deliProduct?.productPrice!!

        // Calculate waste per value
        val wastePerValue = currentDeliSale.differenceWeight * totalPrice

        val updatedSale = DeliSale(
            employeeId = currentDeliSale.employeeId,
            deliProduct = currentDeliSale.deliProduct,
            salePrice = currentDeliSale.deliProduct.calculateTotalPrice(),
            saleWeight = currentDeliSale.saleWeight,
            wastePerSale = 0.0,
            wastePerSaleValue = wastePerValue,
            differenceWeight = currentDeliSale.differenceWeight,
            saleType = currentDeliSale.saleType, // Adjust as per your logic
            quantity = currentDeliSale.deliProduct.totalQuantity(),
            handMade = currentDeliSale.handMade  // Set based on your logic
        )

        Log.d("ViewModel", "Updated DeliSale: $updatedSale")
        return updatedSale
    }

}


