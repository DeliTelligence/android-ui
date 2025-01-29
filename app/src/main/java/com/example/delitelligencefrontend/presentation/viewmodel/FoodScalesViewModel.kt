/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video
https://chatgpt.com
prompt: 'How to build a view model in android, Im using jetpack compose, Dagger Hilt and im using this Kotlin Interface
package com.example.delitelligencefrontend.domain

import com.example.delitelligencefrontend.model.WeightResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface WeightApiService {
    @POST("start")
    suspend fun startWeightReading(): Response<ResponseBody>

    @GET("weight")
    suspend fun getWeightData(): Response<WeightResponse>
}'*/

package com.example.delitelligencefrontend.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delitelligencefrontend.model.StatusResponse
import com.example.delitelligencefrontend.domain.interfaces.WeightApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

@HiltViewModel
class FoodScalesViewModel @Inject constructor(
    private val retrofit: Retrofit
) : ViewModel() {

    private val weightApiService: WeightApiService = retrofit.create(WeightApiService::class.java)

    private val _weight = MutableLiveData<Double>()
    val weight: LiveData<Double> get() = _weight

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> get() = _status

    val loadingState = MutableLiveData<Boolean>()
    val connectedState = MutableLiveData<Boolean>()
    val notificationsEnabledState = MutableLiveData<Boolean>()

    init {
        connectedState.value = false
        notificationsEnabledState.value = false

        connectToScale()
    }

    fun connectToScale() {
        loadingState.value = true
        _status.value = "Connecting to scale..."
        viewModelScope.launch {
            try {
                val response = weightApiService.connectToScale()
                handleResponse(response, "Connected to scale", "Failed to connect to scale")
                connectedState.postValue(response.isSuccessful)
            } catch (e: Exception) {
                handleError("Network Error: ${e.message}")
                connectedState.postValue(false)
            } finally {
                loadingState.postValue(false)
            }
        }
    }

    fun enableNotifications() {
        if (connectedState.value == true) {
            loadingState.value = true
            _status.value = "Enabling notifications..."
            viewModelScope.launch {
                try {
                    val response = weightApiService.enableNotifications()
                    handleResponse(response, "Notifications enabled", "Failed to enable notifications")
                    notificationsEnabledState.postValue(response.isSuccessful)
                } catch (e: Exception) {
                    handleError("Network Error: ${e.message}")
                    notificationsEnabledState.postValue(false)
                } finally {
                    loadingState.postValue(false)
                }
            }
        } else {
            _status.value = "Not connected to scale"
        }
    }

    fun fetchWeightData() {
        if (connectedState.value == true && notificationsEnabledState.value == true) {
            loadingState.value = true
            viewModelScope.launch {
                try {
                    val response = weightApiService.getWeightData()
                    if (response.isSuccessful) {
                        response.body()?.let { weightResponse ->
                            _weight.postValue(weightResponse.weight.toDouble())
                            Log.d("FoodScalesViewModel", "Weight successfully fetched: ${weightResponse.weight}")
                        } ?: run {
                            handleError("Error: Response body is null")
                        }
                    } else {
                        handleError("Error fetching weight data: ${response.code()}")
                    }
                } catch (e: Exception) {
                    handleError("Error fetching weight data: ${e.message}")
                } finally {
                    loadingState.postValue(false)
                }
            }
        } else {
            _status.value = "Not connected or notifications not enabled"
        }
    }

    fun tareScale() {
        if (connectedState.value == true) {
            loadingState.value = true
            _status.value = "Taring scale..."
            viewModelScope.launch {
                try {
                    val response = weightApiService.tareScale()
                    handleResponse(response, "Scale tared", "Failed to tare scale")
                } catch (e: Exception) {
                    handleError("Network Error: ${e.message}")
                } finally {
                    loadingState.postValue(false)
                }
            }
        } else {
            _status.value = "Not connected to scale"
        }
    }

    fun disconnectFromScale() {
        if (connectedState.value == true) {
            loadingState.value = true
            _status.value = "Disconnecting from scale..."
            viewModelScope.launch {
                try {
                    val response = weightApiService.disconnectFromScale()
                    handleResponse(response, "Disconnected from scale", "Failed to disconnect from scale")
                    if (response.isSuccessful) {
                        connectedState.postValue(false)
                        notificationsEnabledState.postValue(false)
                    }
                } catch (e: Exception) {
                    handleError("Network Error: ${e.message}")
                } finally {
                    loadingState.postValue(false)
                }
            }
        } else {
            _status.value = "Not connected to scale"
        }
    }

    private fun handleResponse(response: Response<StatusResponse>, successMessage: String, errorMessage: String) {
        if (response.isSuccessful) {
            Log.d("FoodScalesViewModel", successMessage)
            _status.postValue(successMessage)
        } else {
            Log.e("FoodScalesViewModel", "$errorMessage: ${response.code()}")
            _status.postValue("$errorMessage: ${response.code()}")
        }
    }

    private fun handleError(errorMessage: String) {
        Log.e("FoodScalesViewModel", errorMessage)
        _status.postValue(errorMessage)
    }
}