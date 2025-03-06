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
import com.example.delitelligencefrontend.domain.ProductsUseCase
import com.example.delitelligencefrontend.domain.DeliSaleUseCase
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.domain.interfaces.WeightApiService
import com.example.delitelligencefrontend.enumformodel.PortionType
import com.example.delitelligencefrontend.enumformodel.ProductType
import com.example.delitelligencefrontend.enumformodel.StandardType
import com.example.delitelligencefrontend.model.DeliProduct
import com.example.delitelligencefrontend.model.DeliSale
import com.example.delitelligencefrontend.model.Session
import com.example.delitelligencefrontend.model.mapper.DeliSaleMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: ProductsUseCase,
    private val weightApiService: WeightApiService,
    private val postDeliSaleUseCase: DeliSaleUseCase,
    private val session: Session

) : ViewModel() {

    private val _hotFoodProductsFilling = MutableStateFlow<List<Product>>(emptyList())
    val hotFoodProductsFilling: StateFlow<List<Product>> = _hotFoodProductsFilling

    private val _saladProduct = MutableStateFlow<List<Product>>(emptyList())
    val saladProduct: StateFlow<List<Product>> = _saladProduct

    private val _breakfastProducts = MutableStateFlow<List<Product>>(emptyList())
    val breakfastProducts: StateFlow<List<Product>> = _breakfastProducts

    private val _apiStatus = MutableStateFlow("")
    val apiStatus: StateFlow<String> = _apiStatus

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



    init {
        fetchAllProducts()
//        connectToScaleAndEnableNotifications()
    }

     fun fetchAllProducts() {
        viewModelScope.launch {
            try {
                val fetchedProducts = getProductsUseCase.execute()
                _allProducts.value = fetchedProducts
                _coldFoodProducts.value = fetchedProducts.filter { it.productType?.equals(ProductType.MADE_FOOD_COLD) == true }
                _hotFoodProducts.value = fetchedProducts.filter { it.productType?.equals(ProductType.MADE_FOOD_HOT) == true }
                _mainFillingProducts.value = fetchedProducts.filter { it.productType?.equals(ProductType.MAIN_FILLING_FOOD) == true }
                _fillingProducts.value = fetchedProducts.filter { it.productType?.equals(ProductType.COLD_FOOD) == true }
                _hotFoodProductsFilling.value = fetchedProducts.filter { it.productType?.equals(ProductType.HOT_FOOD) == true }
                _breakfastProducts.value = fetchedProducts.filter { it.productType?.equals(ProductType.BREAKFAST_FOOD) == true }
                _saladProduct.value = fetchedProducts.filter { it.productType?.equals(ProductType.BREAKFAST_FOOD) == true }


            } catch (e: Exception) {
                _apiStatus.value = "Error fetching products: ${e.message}"
            }
        }
    }


    fun postDeliSale(deliSale: DeliSale) {
        viewModelScope.launch {
            try {
                // Map to DeliSaleInputDto using MapStruct
                val inputDto = DeliSaleMapper.INSTANCE.toDeliSaleInputDto(deliSale)

                // Extract and print the DeliProduct ID after mapping
                val deliProductId = inputDto.deliProductInputDto.productInputDto.id
                Log.d("ViewModel", "Mapped DeliProduct ID: $deliProductId")
                println("Mapped DeliProduct ID: $deliProductId")  // For additional verification

                val response = postDeliSaleUseCase.execute(inputDto)
                if (response != null) {
                    _apiStatus.value = "Sale posted successfully: $response"
                } else {
                    _apiStatus.value = "Failed to post sale"
                }
            } catch (e: Exception) {
                _apiStatus.value = "Error posting sale: ${e.message}"
            }
        }
    }



    fun updateCurrentDeliSale(currentDeliSale: DeliSale): DeliSale {
        Log.d("ViewModel", "Updating Current DeliSale: $currentDeliSale")
        var totalPrice = 0.0
        // Iterate over the products and sum their prices with null assertion

        // Add deliProduct's price
        totalPrice += currentDeliSale.deliProduct.calculateTotalPrice()


        // Calculate waste per value
        val wastePerValue = (currentDeliSale.differenceWeight * totalPrice)/1000

        val updatedSale = DeliSale(
            employeeId = currentDeliSale.employeeId,
            deliProduct = currentDeliSale.deliProduct,
            salePrice = totalPrice,
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

    fun getEmployeeId(): String? {
        return session.getUser()?.employeeId
    }

    suspend fun getProductByName(productName: String): Product?{
        return getProductsUseCase.executeProductByName(productName)
    }

}


