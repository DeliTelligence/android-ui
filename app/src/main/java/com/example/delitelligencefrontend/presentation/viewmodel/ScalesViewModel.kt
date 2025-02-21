package com.example.delitelligencefrontend.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delitelligencefrontend.domain.DeliSaleUseCase
import com.example.delitelligencefrontend.domain.interfaces.WeightApiService
import com.example.delitelligencefrontend.enumformodel.StandardType
import com.example.delitelligencefrontend.model.DeliProduct
import com.example.delitelligencefrontend.model.DeliSale
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.model.mapper.DeliSaleMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class ScalesViewModel @Inject constructor(
    private val weightApiService: WeightApiService,
    private val postDeliSaleUseCase: DeliSaleUseCase

    ): ViewModel() {

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

    private val _foodWeightValue = MutableStateFlow<DeliSale?>(null)
    val foodWeightValue: StateFlow<DeliSale?> = _foodWeightValue


    init {
        connectToScaleAndEnableNotifications()
    }


     fun connectToScaleAndEnableNotifications() {
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

    fun fetchWeightData(deliProduct: DeliProduct, standardType: StandardType) {
        if (_isScaleConnected.value == true && _areNotificationsEnabled.value == true) {
            _isWeighing.value = true
            viewModelScope.launch {
                try {
                    val response = weightApiService.getWeightData()
                    if (response.isSuccessful) {
                        response.body()?.let { weightResponse ->
                            val actualWeight = weightResponse.weight.toDouble()
                            _displayedValue.value = Pair(actualWeight, false)

                            // Calculate expected weight based on portionType
                            var expectedWeight = deliProduct.totalCombinedWeight(standardType)

                            // Calculate weight error
                            val error = actualWeight - expectedWeight

                            // Check if error is significant (more than 10%)
                            val isSignificantError = expectedWeight != 0.0 && abs(error / expectedWeight) > 0.1
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

    fun fetchWeightData(onWeightFetched: (Double) -> Unit) {
        if (_isScaleConnected.value && _areNotificationsEnabled.value) {
            _isWeighing.value = true
            viewModelScope.launch {
                try {
                    val response = weightApiService.getWeightData()
                    if (response.isSuccessful) {
                        response.body()?.let { weightResponse ->
                            val actualWeight = weightResponse.weight.toDouble()
                            _displayedValue.value = Pair(actualWeight, false)

                            // Invoke the callback with the fetched weight
                            onWeightFetched(actualWeight)
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

    fun setCurrentDeliSale(deliSale: DeliSale) {
        _currentDeliSale.value = deliSale
        Log.d("ViewModel", "Set Current DeliSale: $deliSale")
    }





}