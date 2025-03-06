package com.example.delitelligencefrontend.domain

import com.example.delitelligencefrontend.domain.interfaces.AnalysisClient
import com.example.delitelligencefrontend.model.DailySaleData
import com.example.delitelligencefrontend.model.HandMadeOrNotSalesData
import com.example.delitelligencefrontend.model.QuantitySalesData
import com.example.delitelligencefrontend.model.SalesTypeData
import com.example.delitelligencefrontend.model.GraphData
import com.example.delitelligencefrontend.model.InventoryAdjustmentData
import com.example.delitelligencefrontend.model.InventoryLevel

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

    suspend fun executeSaleByQuantity(startDate: String, endDate: String): List<QuantitySalesData> {
        return analysisClient.salesByQuantity(startDate, endDate)
    }

    suspend fun executeSaleByType(startDate: String, endDate: String): List<SalesTypeData> {
        return analysisClient.salesByType(startDate, endDate)
    }

    suspend fun executeSaleByHandMadeOrNot(startDate: String, endDate: String): List<HandMadeOrNotSalesData> {
        return analysisClient.salesByHandMadeOrNot(startDate, endDate)
    }

    suspend fun executeSendReport(to: String, startDate: String, endDate: String): String {
        return analysisClient.sendSalesReport(to, startDate, endDate)
    }
    suspend fun executeSalesPrediction(): List<GraphData> {
        return analysisClient.getSalesPrediction()
    }

    suspend fun executeGetTotalValue(): Double {
        return analysisClient.getTotalInventoryValue()
    }
    suspend fun executeGetTotalWeight(): Double {
        return analysisClient.getTotalWeight()
    }

    suspend fun executeGetAdjustmentData(): List<InventoryAdjustmentData> {
        return analysisClient.getAdjustmentData()
    }
    suspend fun executeGetInventoryLevelData(): List<InventoryLevel> {
        return analysisClient.getInventoryLevels()
    }



}