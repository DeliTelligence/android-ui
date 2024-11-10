/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video
https://chatgpt.com
prompt: 'How to build a view model in android, Im using jetpack compose, Dagger Hilt and im using this Kotlin Interface
package com.example.delitelligencefrontend.domain

import com.example.delitelligencefrontend.data.WeightResponse
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FoodScalesViewModel @Inject constructor() : ViewModel() {

    private val client = OkHttpClient()

    private val _weight = MutableLiveData<Double>()
    val weight: LiveData<Double> get() = _weight

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> get() = _status

    val loadingState = MutableLiveData<Boolean>()
    val startedState = MutableLiveData<Boolean>()

    init {
        startedState.value = false // Ensure it starts as false
    }

    fun startWeightReading() {
        loadingState.value = true
        _status.value = "Starting weight reading..."
        viewModelScope.launch {
            try {
                val response = startWeightReadingRequest()
                if (response.isSuccessful) {
                    Log.d("FoodScalesViewModel", "Weight reading started successfully")
                    _status.postValue("Weight reading started")
                    loadingState.postValue(false)
                    startedState.postValue(true) // Mark that we have started reading
                } else {
                    Log.e("FoodScalesViewModel", "Failed to start weight reading: ${response.code}")
                    _status.postValue("Failed to start weight reading: ${response.code}")
                    loadingState.postValue(false)
                    startedState.postValue(false)
                }
            } catch (e: IOException) {
                Log.e("FoodScalesViewModel", "Network Error: ${e.message}")
                _status.postValue("Network Error: ${e.message}")
                loadingState.postValue(false)
                startedState.postValue(false)
            }
        }
    }

    private suspend fun startWeightReadingRequest(): Response {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("http://172.20.176.1:5000/start")
                .post(RequestBody.create(null, ByteArray(0)))
                .build()
            client.newCall(request).execute()
        }
    }

    fun fetchWeightData() {
        if (startedState.value == true) {
            loadingState.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val request = Request.Builder()
                    .url("http://172.20.176.1:5000/weight")
                    .get()
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("FoodScalesViewModel", "Error fetching weight data: ${e.message}")
                        _status.postValue("Error fetching weight data")
                        loadingState.postValue(false)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        try {
                            response.use {
                                if (it.isSuccessful) {
                                    val responseData = it.body?.string()
                                    if (responseData != null) {
                                        val jsonObject = JSONObject(responseData)
                                        val weight = jsonObject.getDouble("weight")
                                        _weight.postValue(weight)
                                        Log.d("FoodScalesViewModel", "Weight successfully fetched: $weight")
                                    } else {
                                        Log.e("FoodScalesViewModel", "Response body is null")
                                        _status.postValue("Error: Response body is null")
                                    }
                                } else {
                                    Log.e("FoodScalesViewModel", "Error response code: ${it.code}")
                                    _status.postValue("Error fetching weight data: ${it.code}")
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("FoodScalesViewModel", "Exception in onResponse: ${e.message}")
                            _status.postValue("Error parsing weight data")
                            loadingState.postValue(false)
                        } finally {
                            loadingState.postValue(false)
                        }
                    }
                })
            }
        } else {
            Log.e("FoodScalesViewModel", "Weight reading not started")
            _status.postValue("Weight reading has not been started")
        }
    }
}