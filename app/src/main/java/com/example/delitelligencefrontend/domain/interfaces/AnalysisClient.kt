package com.example.delitelligencefrontend.domain.interfaces

import com.example.delitelligencefrontend.model.DailySaleData

interface AnalysisClient {
    suspend fun salesInAGivenPeriod(startDate: String, endDate: String): List<DailySaleData>
    suspend fun dailySaleTotal(date: String): Double
    suspend fun  totalSales(): Double

}