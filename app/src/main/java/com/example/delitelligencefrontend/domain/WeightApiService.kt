/*https://chatgpt.com
 prompt 'whats the best way to send requests to a service using REST using OkayHttp library in Android'
 Date Sent: 6/11/2024 */

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
}