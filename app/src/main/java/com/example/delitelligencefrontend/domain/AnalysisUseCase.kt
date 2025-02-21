package com.example.delitelligencefrontend.domain

import com.example.delitelligencefrontend.data.ApolloAnalysisClient
import com.example.delitelligencefrontend.domain.interfaces.AnalysisClient
import com.example.delitelligencefrontend.model.DailySaleData

class AnalysisUseCase(
    private val analysisClient: AnalysisClient
) {
    suspend fun execute(startDate: String, endDate: String): List<DailySaleData> {
        return analysisClient.salesInAGivenPeriod(startDate, endDate)
    }

    suspend fun executeDailySalesTotal(date: String): Double {
        return analysisClient.dailySaleTotal(date)
    }

    suspend fun execute(): Double {
        return analysisClient.totalSales()
    }

}