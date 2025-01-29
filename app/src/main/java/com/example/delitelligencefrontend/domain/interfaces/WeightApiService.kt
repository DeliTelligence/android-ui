/*https://chatgpt.com
 prompt 'whats the best way to send requests to a service using REST using OkayHttp library in Android'
 Date Sent: 6/11/2024 */

package com.example.delitelligencefrontend.domain.interfaces

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
}